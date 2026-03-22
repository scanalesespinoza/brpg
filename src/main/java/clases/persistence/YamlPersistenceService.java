package clases.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class YamlPersistenceService {
    private static final String DATA_DIR = "data";
    private final ObjectMapper mapper;
    private final Map<String, List<Map<String, Object>>> tables = new ConcurrentHashMap<>();

    public YamlPersistenceService() {
        this.mapper = new ObjectMapper(new YAMLFactory());
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            initializeDefaultData(); // Create initial data if empty
        } else {
            loadTables();
        }
    }

    private void loadTables() {
        String[] tableNames = { "habilidad", "mision", "objeto", "personaje", "mob", "inventario", "jugador", "encargo",
                "contrincante_habilidad", "texto", "dialogo_mision", "dialogo_personaje", "equipo", "objeto_mision",
                "cuenta" };
        for (String tableName : tableNames) {
            File file = new File(DATA_DIR, tableName + ".yaml");
            if (file.exists()) {
                try {
                    List<Map<String, Object>> data = mapper.readValue(file, List.class);
                    tables.put(tableName, data);
                    System.out.println("Loaded " + data.size() + " rows for " + tableName);
                } catch (IOException e) {
                    System.err.println("Error loading " + tableName + ": " + e.getMessage());
                    tables.put(tableName, new ArrayList<>());
                }
            } else {
                tables.put(tableName, new ArrayList<>());
            }
        }
    }

    // Async save
    public void saveData(String tableName) {
        CompletableFuture.runAsync(() -> {
            try {
                File file = new File(DATA_DIR, tableName + ".yaml");
                mapper.writeValue(file, tables.get(tableName));
                // System.out.println("Saved " + tableName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Map<String, Object>> getTable(String tableName) {
        return tables.getOrDefault(tableName, new ArrayList<>());
    }

    public void updateTable(String tableName, List<Map<String, Object>> newData) {
        tables.put(tableName, newData);
        saveData(tableName);
    }

    // Generic DAO Loader for Jackson
    public <T> List<T> getTableAs(String tableName, Class<T> clazz) {
        File file = new File(DATA_DIR, tableName + ".yaml");
        if (!file.exists()) return new ArrayList<>();
        try {
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return mapper.readValue(file, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Rudimentary SQL parser/executor replacement
    public MockResultSet executeQuery(String sql) {
        String lowerSql = sql.toLowerCase().trim();

        if (lowerSql.startsWith("select")) {
            return handleSelect(sql);
        }
        return new MockResultSet(new ArrayList<>());
    }

    public int executeUpdate(String sql) {
        String lowerSql = sql.toLowerCase().trim();
        if (lowerSql.startsWith("update")) {
            return handleUpdate(sql);
        } else if (lowerSql.startsWith("insert")) {
            return handleInsert(sql);
        } else if (lowerSql.startsWith("delete")) {
            return handleDelete(sql);
        }
        return 0;
    }

    // --- Query Handlers ---

    private MockResultSet handleSelect(String sql) {
        String lowerSql = sql.toLowerCase();

        // Match specific queries seen in Manager.java and others
        if (lowerSql.contains("from habilidad")) {
            if (lowerSql.contains("where id=")) {
                String idStr = lowerSql.split("where id=")[1].trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("habilidad"), "id", id);
            }
            return new MockResultSet(tables.get("habilidad"));
        } else if (lowerSql.contains("from mision")) {
            // check if filtered by personaje_id
            if (lowerSql.contains("where personaje_id=")) {
                String idStr = sql.substring(sql.lastIndexOf("=") + 1).trim();
                int pid = Integer.parseInt(idStr);
                List<Map<String, Object>> all = tables.get("mision");
                List<Map<String, Object>> filtered = new ArrayList<>();
                for (Map<String, Object> row : all) {
                    Object pIdObj = row.get("personaje_id");
                    if (pIdObj != null && Integer.parseInt(pIdObj.toString()) == pid) {
                        filtered.add(row);
                    }
                }
                return new MockResultSet(filtered);
            }
            if (lowerSql.contains("where id=")) {
                String idStr = lowerSql.split("where id=")[1].trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("mision"), "id", id);
            }
            return new MockResultSet(tables.get("mision"));
        } else if (lowerSql.contains("from objeto_mision")) {
            return new MockResultSet(tables.get("objeto_mision"));
        } else if (lowerSql.contains("from objeto")) {
            if (lowerSql.contains("where id=")) {
                String idStr = lowerSql.split("where id=")[1].trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("objeto"), "id", id);
            }
            return new MockResultSet(tables.get("objeto"));
        } else if (lowerSql.contains("from personaje") && lowerSql.contains("mob")) {
            // GENERIC JOIN Personaje + Mob
            // "WHERE p.id = m.personaje_id"
            // Since we are mocking, let's just return mobs with their Personaje data
            // merged.
            List<Map<String, Object>> mobs = tables.get("mob");
            List<Map<String, Object>> personajes = tables.get("personaje");
            List<Map<String, Object>> joined = new ArrayList<>();

            for (Map<String, Object> mob : mobs) {
                int mobId = getId(mob, "personaje_id");
                // Find matching personaje
                Map<String, Object> matchP = null;
                for (Map<String, Object> p : personajes) {
                    if (getId(p, "id") == mobId) {
                        matchP = p;
                        break;
                    }
                }

                if (matchP != null) {
                    Map<String, Object> row = new HashMap<>();
                    row.putAll(matchP);
                    row.putAll(mob);
                    row.put("Id", mobId); // Ensure ID is present
                    joined.add(row);
                }
            }
            return new MockResultSet(joined);

        } else if (lowerSql.contains("from personaje pjuno") && lowerSql.contains("jugador pjdos")) {
            // JOIN Personaje + Jugador for loading Main Player
            String idStr = sql.substring(sql.lastIndexOf("=") + 1).trim();
            int id = Integer.parseInt(idStr);

            List<Map<String, Object>> personajes = tables.get("personaje");
            List<Map<String, Object>> jugadores = tables.get("jugador");

            Map<String, Object> pRow = null;
            for (Map<String, Object> r : personajes) {
                if (getId(r, "id") == id) {
                    pRow = r;
                    break;
                }
            }

            Map<String, Object> jRow = null;
            for (Map<String, Object> r : jugadores) {
                // Jugador usually links via character ID? schema says "Personaje_id" or "id" if
                // 1:1.
                // In initialization we put same ID. user query says "pjdos.Personaje_id=" + id
                // But in initializeDefaultData we used "id" for jugador table too.
                // Let's check "id" first, then "Personaje_id"
                int rId = getId(r, "id");
                if (rId == id) {
                    jRow = r;
                    break;
                }
                // fallback check if we stored it as personaje_id
                Object pid = r.get("personaje_id");
                if (pid != null && Integer.parseInt(pid.toString()) == id) {
                    jRow = r;
                    break;
                }
            }

            if (pRow != null && jRow != null) {
                Map<String, Object> result = new HashMap<>();
                result.putAll(pRow);
                result.putAll(jRow);

                // DATA MAPPING / ALIASING
                // pjuno.posicionX posX, pjuno.posicionY posY
                result.put("posX", pRow.get("posicionx"));
                result.put("posY", pRow.get("posiciony"));

                // pjdos.totalPuntosHabilidad ptosHab, pjdos.totalPuntosEstadistica ptosEst
                // pjdos.limiteSuperiorExperiencia limExp
                // pjdos.estaBaneado ban, pjdos.Cuenta_id cuenta
                result.put("ptosHab", jRow.get("totalPuntosHabilidad")); // In init data we didn't set this? handle null
                result.put("ptosEst", jRow.get("totalPuntosEstadistica"));
                result.put("limExp", jRow.get("limiteSuperiorExperiencia"));
                result.put("ban", jRow.get("estaBaneado"));
                result.put("cuenta", jRow.get("Cuenta_id"));
                result.put("vit", jRow.get("vitalidad"));
                result.put("des", jRow.get("destreza"));
                result.put("sab", jRow.get("sabiduria"));
                result.put("fue", jRow.get("fuerza"));
                result.put("peso", jRow.get("pesoSoportado"));

                // Ensure defaults to avoid NPE if YAML missing keys
                if (result.get("limExp") == null)
                    result.put("limExp", 1000);
                if (result.get("ptosHab") == null)
                    result.put("ptosHab", 0);
                if (result.get("ptosEst") == null)
                    result.put("ptosEst", 0);

                List<Map<String, Object>> resList = new ArrayList<>();
                resList.add(result);
                return new MockResultSet(resList);
            }

        } else if (lowerSql.contains("from personaje")) {
            if (lowerSql.contains("id=")) {
                // Single fetch
                String idStr = lowerSql.substring(lowerSql.lastIndexOf("=") + 1).trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("personaje"), "id", id);
            }
            // Filter "WHERE tipo = 2 OR tipo= 1"
            if (lowerSql.contains("tipo = 2") || lowerSql.contains("tipo= 1")) {
                List<Map<String, Object>> filtered = new ArrayList<>();
                for (Map<String, Object> row : tables.get("personaje")) {
                    int tipo = getId(row, "tipo");
                    if (tipo == 1 || tipo == 2) {
                        filtered.add(row);
                    }
                }
                return new MockResultSet(filtered);
            }
        } else if (lowerSql.contains("from inventario")) {
            // WHERE Personaje_id=...
            if (lowerSql.contains("personaje_id=")) {
                // simplistic parsing
                String[] parts = lowerSql.split("personaje_id=");
                String after = parts[1].split(" ")[0]; // grab number
                // might be "1 AND ..."
                if (after.contains("and")) {
                    after = after.split("and")[0].trim();
                    // AND Objeto_id=... handled?
                    // There is "SELECT * FROM inventario WHERE Personaje_id="+idJugador+" AND
                    // Objeto_id="+idItem
                    if (lowerSql.contains("objeto_id=")) {
                        int pid = Integer.parseInt(after);
                        String after2 = lowerSql.split("objeto_id=")[1].trim();
                        int oid = Integer.parseInt(after2);
                        // filter double
                        List<Map<String, Object>> res = new ArrayList<>();
                        for (Map<String, Object> r : tables.get("inventario")) {
                            if (getId(r, "personaje_id") == pid && getId(r, "objeto_id") == oid) {
                                res.add(r);
                            }
                        }
                        return new MockResultSet(res);
                    }
                }

                int pid = Integer.parseInt(after.trim());
                List<Map<String, Object>> res = new ArrayList<>();
                for (Map<String, Object> r : tables.get("inventario")) {
                    if (getId(r, "personaje_id") == pid) {
                        res.add(r);
                    }
                }
                return new MockResultSet(res);
            }
        } else if (lowerSql.contains("from encargo")) {
            // WHERE personaje_id = ... AND updated_at IS [NOT] NULL
            int pid = 0;
            if (lowerSql.contains("personaje_id=")) {
                String sub = lowerSql.split("personaje_id=")[1].trim();
                pid = Integer.parseInt(sub.split(" ")[0]);
            } else if (lowerSql.contains("personaje_id =")) {
                String sub = lowerSql.split("personaje_id =")[1].trim();
                pid = Integer.parseInt(sub.split(" ")[0]);
            } else if (lowerSql.contains("personaje_id")) {
                // Try to find the number after personaje_id
                String sub = lowerSql.split("personaje_id")[1].trim();
                if (sub.startsWith("="))
                    sub = sub.substring(1).trim();
                pid = Integer.parseInt(sub.split(" ")[0]);
            }

            boolean isDone = lowerSql.contains("updated_at is not null");
            List<Map<String, Object>> res = new ArrayList<>();
            for (Map<String, Object> r : tables.get("encargo")) {
                if (getId(r, "personaje_id") == pid) {
                    Object up = r.get("updated_at");
                    if (isDone) {
                        if (up != null && !up.toString().equalsIgnoreCase("null"))
                            res.add(r);
                    } else {
                        if (up == null || up.toString().equalsIgnoreCase("null"))
                            res.add(r);
                    }
                }
            }
            return new MockResultSet(res);

        } else if (lowerSql.contains("from equipo")) {
            // WHERE id_objeto = ...
            int id = 0;
            if (lowerSql.contains("id_objeto=")) {
                String idStr = lowerSql.split("id_objeto=")[1].trim();
                id = Integer.parseInt(idStr);
            } else if (lowerSql.contains("id_objeto =")) {
                String idStr = lowerSql.split("id_objeto =")[1].trim();
                id = Integer.parseInt(idStr.split(" ")[0]);
            }
            if (id != 0)
                return filterById(tables.get("equipo"), "id_objeto", id);
            return new MockResultSet(tables.get("equipo"));
        } else if (lowerSql.contains("from dialogo_personaje")) {
            // JOIN with texto
            // WHERE dial.personaje_id = ...
            int id = 0;
            if (lowerSql.contains("personaje_id")) {
                String sub = lowerSql.split("personaje_id")[1].replace("=", "").trim();
                id = Integer.parseInt(sub.split(" ")[0]);
            }

            List<Map<String, Object>> res = new ArrayList<>();
            List<Map<String, Object>> dialogs = tables.get("dialogo_personaje");
            List<Map<String, Object>> texts = tables.get("texto");

            for (Map<String, Object> d : dialogs) {
                if (getId(d, "personaje_id") == id) {
                    // join
                    int textId = getId(d, "texto_id");
                    for (Map<String, Object> t : texts) {
                        if (getId(t, "id") == textId) {
                            Map<String, Object> joined = new HashMap<>(d);
                            // keys expected: txt.id, txt.texto, dial.texto_siguiente_id
                            joined.put("txt.id", t.get("id"));
                            joined.put("txt.texto", t.get("texto"));
                            joined.put("dial.texto_siguiente_id", d.get("texto_siguiente_id"));
                            res.add(joined);
                            break;
                        }
                    }
                }
            }
            return new MockResultSet(res);

        } else if (lowerSql.contains("from dialogo_mision")) {
            // JOIN with texto
            // WHERE dial.mision_id = ...
            String idStr = lowerSql.split("mision_id")[1].replace("=", "").trim();
            int id = Integer.parseInt(idStr);

            List<Map<String, Object>> res = new ArrayList<>();
            List<Map<String, Object>> dialogs = tables.get("dialogo_mision");
            List<Map<String, Object>> texts = tables.get("texto");

            for (Map<String, Object> d : dialogs) {
                if (getId(d, "mision_id") == id) {
                    // join
                    int textId = getId(d, "texto_id");
                    for (Map<String, Object> t : texts) {
                        if (getId(t, "id") == textId) {
                            Map<String, Object> joined = new HashMap<>(d);
                            // keys expected: txt.id, txt.texto, dial.texto_siguiente_id,
                            // dial.texto_anterior_id
                            joined.put("txt.id", t.get("id"));
                            joined.put("txt.texto", t.get("texto"));
                            joined.put("dial.texto_siguiente_id", d.get("texto_siguiente_id"));
                            joined.put("dial.texto_anterior_id", d.get("texto_anterior_id"));
                            res.add(joined);
                            break;
                        }
                    }
                }
            }
            return new MockResultSet(res);
        } else if (lowerSql.contains("from contrincante_habilidad")) {
            // SELECT * FROM contrincante_habilidad WHERE personaje_id = ...
            if (lowerSql.contains("personaje_id=")) {
                String idStr = lowerSql.split("personaje_id=")[1].trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("contrincante_habilidad"), "personaje_id", id);
            }
            return new MockResultSet(tables.get("contrincante_habilidad"));
        } else if (lowerSql.contains("from inventario") && lowerSql.contains("objeto")) {
            // JOIN Inventario + Objeto
            // SELECT ... FROM inventario inv, objeto obj WHERE inv.Objeto_id = obj.id ...
            int pid = 0;
            if (lowerSql.contains("personaje_id=")) {
                String sub = lowerSql.split("personaje_id=")[1].trim();
                pid = Integer.parseInt(sub.split(" ")[0]);
            } else if (lowerSql.contains("personaje_id =")) {
                String sub = lowerSql.split("personaje_id =")[1].trim();
                pid = Integer.parseInt(sub.split(" ")[0]);
            }

            List<Map<String, Object>> joined = new ArrayList<>();
            List<Map<String, Object>> invList = tables.get("inventario");
            List<Map<String, Object>> objList = tables.get("objeto");

            for (Map<String, Object> inv : invList) {
                if (getId(inv, "personaje_id") == pid || pid == 0) {
                    int oid = getId(inv, "objeto_id");
                    for (Map<String, Object> obj : objList) {
                        if (getId(obj, "id") == oid) {
                            Map<String, Object> row = new HashMap<>(inv);
                            // Merge object data. Prefix collision handling?
                            // Inventario.java aliases: inv.Personaje_id, inv.Objeto_id, etc.
                            // But it explicitly asks for columns. MockResultSet maps by column name.
                            row.putAll(obj);
                            // Explicitly put keys expected by Inventario.java if needed (usually just name
                            // matching)
                            joined.add(row);
                            break;
                        }
                    }
                }
            }
            return new MockResultSet(joined);

        } else if (lowerSql.contains("from inventario")) {
            // SELECT * FROM inventario WHERE personaje_id = ...
            if (lowerSql.contains("personaje_id=")) {
                String idStr = lowerSql.split("personaje_id=")[1].trim();
                int id = Integer.parseInt(idStr);
                return filterById(tables.get("inventario"), "personaje_id", id);
            }
            return new MockResultSet(tables.get("inventario"));
        }

        return new MockResultSet(new ArrayList<>());
    }

    private int handleUpdate(String sql) {
        String lowerSql = sql.toLowerCase();
        // UPDATE personaje SET posicionX=..., posicionY=... WHERE id=...
        if (lowerSql.startsWith("update personaje")) {
            // simplified parser
            try {
                int whereIdx = lowerSql.indexOf("where id");
                if (whereIdx == -1)
                    whereIdx = lowerSql.indexOf("where id=");
                String idStr = lowerSql.substring(lowerSql.lastIndexOf("=") + 1).trim();
                int id = Integer.parseInt(idStr);

                // extract sets. Very fragile parsing!
                // SET posicionx = 123, posiciony = 456
                String sets = lowerSql.substring(lowerSql.indexOf("set") + 3, lowerSql.indexOf("where"));
                String[] assignments = sets.split(",");

                Map<String, Object> target = null;
                List<Map<String, Object>> pList = tables.get("personaje");
                for (Map<String, Object> row : pList) {
                    if (getId(row, "id") == id) {
                        target = row;
                        break;
                    }
                }

                if (target != null) {
                    for (String assign : assignments) {
                        String[] pair = assign.split("=");
                        String col = pair[0].trim();
                        String val = pair[1].trim();
                        // handle numbers
                        target.put(col, Double.parseDouble(val)); // Store as double mostly? Or logic to parse type.
                        // But wait, "posicionx" is int in schema, but JSON loads as Integer/Double.
                        // DB schema: smallint.
                    }
                    saveData("personaje");
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (lowerSql.startsWith("update inventario")) {
            // Update inventario set cantidad=... WHERE Personaje_id=... AND Objeto_id=...
            try {
                String setPart = lowerSql.substring(lowerSql.indexOf("set") + 3, lowerSql.indexOf("where"));
                String wherePart = lowerSql.substring(lowerSql.indexOf("where") + 5);

                int cantidad = 0;
                if (setPart.contains("cantidad=")) {
                    cantidad = Integer.parseInt(setPart.split("cantidad=")[1].trim().split(" ")[0].replace(",", ""));
                }

                int pid = 0;
                if (wherePart.contains("personaje_id=")) {
                    pid = Integer.parseInt(
                            wherePart.split("personaje_id=")[1].trim().split(" ")[0].replace("and", "").trim());
                }

                int oid = 0;
                if (wherePart.contains("objeto_id=")) {
                    oid = Integer.parseInt(wherePart.split("objeto_id=")[1].trim().split(" ")[0]);
                }

                if (pid != 0 && oid != 0) {
                    for (Map<String, Object> r : tables.get("inventario")) {
                        if (getId(r, "personaje_id") == pid && getId(r, "objeto_id") == oid) {
                            r.put("cantidad", cantidad);
                            saveData("inventario");
                            return 1;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        } else if (lowerSql.startsWith("update encargo")) {
            // UPDATE encargo SET rolpersonaje=..., updated_at=... WHERE ...
            try {
                // Parse WHERE clause to find the row
                // WHERE personaje_id = ... AND mision_id = ... AND created_at = ...
                // This parsing is complex without regex or proper parser.
                // Let's assume the query format from Encargo.java is relatively fixed.

                // Extract values from SET
                String setPart = lowerSql.substring(lowerSql.indexOf("set") + 3, lowerSql.indexOf("where"));
                String[] sets = setPart.split(",");
                // Expect: rolpersonaje=..., updated_at=...

                // Logic to update local cache??
                // Ideally yes, but Encargo.java maintains its own memory state and just pushes
                // to DB.
                // However, subsequent SELECTs might fetch from here.
                // So we SHOULD update "tables.get('encargo')".

                // For now, to unblock, let's just trigger SAVE appropriately if we could modify
                // it.
                // But we don't know WHICH row to modify easily.
                // Given the complexity of parsing arbitrary WHERE clauses, and that
                // Encargo.java logic
                // seemingly re-reads from DB or trusts its own state...
                // Actually Encargo.java reads from DB on "cargarMisiones".

                // Let's do a best-effort update if possible, or just accept it (since we are
                // mocking persistence).
                // If we don't update the YAML in memory, next fetch will be wrong.

                // Let's try to parse the identifiers.
                int pid = 0;
                int mid = 0;
                String created = "";

                if (lowerSql.contains("personaje_id")) {
                    String sub = lowerSql.split("personaje_id")[1].split("=")[1].trim();
                    pid = Integer.parseInt(sub.split(" ")[0]);
                }
                if (lowerSql.contains("mision_id")) {
                    String sub = lowerSql.split("mision_id")[1].split("=")[1].trim();
                    mid = Integer.parseInt(sub.split(" ")[0]);
                }
                // created_at usually string enclosed in quotes? Encargo says: AND created_at =
                // '...'
                if (lowerSql.contains("created_at")) {
                    String sub = lowerSql.split("created_at")[1].split("=")[1].trim();
                    // remove quotes
                    created = sub.replace("'", "").trim();
                    if (created.contains(" ")) { // "2010... ..."
                        // might cut off if we split by space earlier?
                        // regex would be better.
                        // created_at = 'YYYY-MM-DD HH:MM:SS'
                        // The previous logic split keys? No.
                        // Let's take the substring until next AND or end
                        int end = created.indexOf(" and");
                        if (end == -1)
                            created = created; // end of string
                        else
                            created = created.substring(0, end);
                    }
                }

                // Now find the row
                for (Map<String, Object> r : tables.get("encargo")) {
                    if (getId(r, "personaje_id") == pid && getId(r, "mision_id") == mid) {
                        // fuzzy match date?
                        // If created_at is part of the WHERE.
                        if (!created.isEmpty()) {
                            Object c = r.get("created_at");
                            if (c != null && c.toString().equals(created)) {
                                // Update this row
                                applySets(r, sets);
                                saveData("encargo");
                                return 1;
                            }
                        } else {
                            // maybe just match IDs if date not provided? (Rare for encargo)
                            applySets(r, sets);
                            saveData("encargo");
                            return 1;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }

    private void applySets(Map<String, Object> row, String[] sets) {
        for (String s : sets) {
            String[] kv = s.split("=");
            String k = kv[0].trim();
            String v = kv[1].trim();
            if (v.equalsIgnoreCase("null")) {
                row.put(k, null);
            } else {
                row.put(k, v.replace("'", ""));
            }
        }
    }

    private int handleInsert(String sql) {
        String lowerSql = sql.toLowerCase();
        // INSERT INTO inventario VALUES (...)
        if (lowerSql.startsWith("insert into inventario")) {
            // Parse values
            // VALUES (pid, oid, cant, equip)
            try {
                String vals = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));
                String[] parts = vals.split(",");
                Map<String, Object> row = new HashMap<>();
                row.put("personaje_id", Integer.parseInt(parts[0].trim()));
                row.put("objeto_id", Integer.parseInt(parts[1].trim()));
                row.put("cantidad", Integer.parseInt(parts[2].trim()));
                row.put("estaequipado", Integer.parseInt(parts[3].trim()));

                tables.get("inventario").add(row);
                saveData("inventario");
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (lowerSql.startsWith("insert into encargo")) {
            // VALUES (pid, mid, created, rol, updated)
            try {
                String vals = sql.substring(sql.indexOf("VALUES") + 6);
                vals = vals.replace("(", "").replace(")", "").trim();
                // Careful with splitting by comma if text contains comma (unlikely for dates
                // here)
                String[] parts = vals.split(",");
                Map<String, Object> row = new HashMap<>();
                row.put("personaje_id", Integer.parseInt(parts[0].trim()));
                row.put("mision_id", Integer.parseInt(parts[1].trim()));
                row.put("created_at", parts[2].trim().replace("'", ""));
                row.put("rolpersonaje", Integer.parseInt(parts[3].trim()));
                String up = parts[4].trim();
                row.put("updated_at", up.equalsIgnoreCase("null") ? null : up.replace("'", ""));

                tables.get("encargo").add(row);
                saveData("encargo");
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int handleDelete(String sql) {
        String lowerSql = sql.toLowerCase();
        if (lowerSql.startsWith("delete from encargo")) {
            try {
                // WHERE personaje_id = ... AND mision_id = ... AND created_at = ...
                int pid = 0;
                int mid = 0;
                String created = "";

                if (lowerSql.contains("personaje_id")) {
                    String sub = lowerSql.split("personaje_id")[1].split("=")[1].trim();
                    pid = Integer.parseInt(sub.split(" ")[0]);
                }
                if (lowerSql.contains("mision_id")) {
                    String sub = lowerSql.split("mision_id")[1].split("=")[1].trim();
                    mid = Integer.parseInt(sub.split(" ")[0]);
                }
                if (lowerSql.contains("created_at")) {
                    String sub = lowerSql.split("created_at")[1].split("=")[1].trim();
                    created = sub.replace("'", "");
                    // clean up suffix
                    int end = created.indexOf(" "); // might match "2010/..."
                    // wait, created_at string is 'YYYY...' logic for splitting earlier was rough
                    // let's grab between quotes if possible
                    if (sql.contains("'")) {
                        String[] q = sql.split("'");
                        // "created_at = 'DATE'"
                        // find the quote after created_at
                        int idx = lowerSql.indexOf("created_at");
                        String after = sql.substring(idx);
                        int firstQ = after.indexOf("'");
                        int secondQ = after.indexOf("'", firstQ + 1);
                        if (firstQ != -1 && secondQ != -1) {
                            created = after.substring(firstQ + 1, secondQ);
                        }
                    }
                }

                Iterator<Map<String, Object>> it = tables.get("encargo").iterator();
                while (it.hasNext()) {
                    Map<String, Object> r = it.next();
                    if (getId(r, "personaje_id") == pid && getId(r, "mision_id") == mid) {
                        if (!created.isEmpty()) {
                            Object c = r.get("created_at");
                            if (c != null && c.toString().equals(created)) {
                                it.remove();
                            }
                        } else {
                            it.remove();
                        }
                    }
                }
                saveData("encargo");
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (lowerSql.startsWith("delete from inventario")) {
            try {
                // WHERE personaje_id = ... AND objeto_id = ...
                int pid = 0;
                int oid = 0;
                if (lowerSql.contains("personaje_id")) {
                    String sub = lowerSql.split("personaje_id")[1].split("=")[1].trim();
                    pid = Integer.parseInt(sub.split(" ")[0]);
                }
                if (lowerSql.contains("objeto_id")) {
                    String sub = lowerSql.split("objeto_id")[1].split("=")[1].trim();
                    oid = Integer.parseInt(sub.split(" ")[0]);
                }

                Iterator<Map<String, Object>> it = tables.get("inventario").iterator();
                while (it.hasNext()) {
                    Map<String, Object> r = it.next();
                    if (getId(r, "personaje_id") == pid && getId(r, "objeto_id") == oid) {
                        it.remove();
                    }
                }
                saveData("inventario");
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int getId(Map<String, Object> row, String col) {
        Object val = row.get(col);
        if (val == null) {
            // try case insensitive
            for (String k : row.keySet()) {
                if (k.equalsIgnoreCase(col)) {
                    Object val2 = row.get(k);
                    if (val2 != null) {
                        return Integer.parseInt(val2.toString());
                    }
                }
            }
            // If still null, return 0 or throw descriptive error
            // System.err.println("Warning: Column '" + col + "' not found in row: " + row);
            return 0;
        }
        return Integer.parseInt(val.toString());
    }

    private MockResultSet filterById(List<Map<String, Object>> table, String colName, int id) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> row : table) {
            if (getId(row, colName) == id) {
                filtered.add(row);
            }
        }
        return new MockResultSet(filtered);
    }

    private void initializeDefaultData() {
        // Populate critical data for game start
        // 1. Jugador (ID 18)
        Map<String, Object> player = new HashMap<>();
        player.put("id", 18);
        player.put("nombre", "Gerald");
        player.put("nivel", 37);
        player.put("posicionx", 200);
        player.put("posiciony", 200);
        player.put("tipo", 0);
        player.put("vitalidad", 231);
        player.put("destreza", 166);
        player.put("sabiduria", 192);
        player.put("fuerza", 166);
        player.put("experiencia", 9993);
        player.put("limiteSuperiorExperiencia", 13477);
        player.put("dinero", 72500);
        player.put("hp", 800); // inferred
        player.put("hpMax", 800);
        player.put("mp", 200);
        player.put("mpMax", 200);

        List<Map<String, Object>> pList = new ArrayList<>();
        pList.add(player);
        tables.put("personaje", pList);

        tables.put("jugador", pList); // Jugador table also has data, usually joined.

        // 2. Mobs
        List<Map<String, Object>> mobs = new ArrayList<>();
        // ID 6 = Goblin
        addMob(pList, mobs, 6, "Goblin", 1, 10, 11, 3, 10, 5, 5, 13, 50, 5);
        addMob(pList, mobs, 7, "Goblin2", 24, 12, 12, 3, 20, 6, 6, 13, 24, 6);
        addMob(pList, mobs, 8, "Tana", 20, 100, 100, 3, 20, 5, 5, 11, 20, 600);
        addMob(pList, mobs, 11, "Wolverine", 5, 200, 200, 3, 15, 6, 6, 7, 5, 1300);
        addMob(pList, mobs, 12, "Boss", 20, 300, 300, 3, 20, 5, 5, 14, 4000, 5000);

        tables.put("mob", mobs);

        // NPCs
        addNpc(pList, 1, "Vendedor 1", 1, 100, 100, 1);
        addNpc(pList, 2, "Vendedor 2", 1, 150, 100, 1);
        addNpc(pList, 3, "Mision 1", 1, 200, 200, 2);
        addNpc(pList, 4, "Mision 2", 1, 250, 200, 2);
        addNpc(pList, 5, "Mision 3", 1, 300, 200, 2);
        addNpc(pList, 13, "Guard", 10, 400, 400, 2);
        addNpc(pList, 14, "Guard 2", 10, 450, 400, 2);
        addNpc(pList, 15, "Viajero", 5, 500, 500, 2);
        addNpc(pList, 16, "Mono", 1, 550, 550, 2);
        addNpc(pList, 17, "Perdido", 1, 600, 600, 2);

        // 3. Objetos (Items)
        List<Map<String, Object>> items = new ArrayList<>();
        // id 1 zapato
        Map<String, Object> i1 = new HashMap<>();
        i1.put("id", 1);
        i1.put("nombre", "zapato");
        i1.put("descripcion", "pisar");
        i1.put("nom_grafico", "zapatos");
        i1.put("tipo", 1);
        i1.put("usocombate", 0);
        i1.put("beneficio", 0);
        i1.put("peso", 1);
        i1.put("valordinero", 10);
        items.add(i1);

        tables.put("objeto", items);

        // 4. Habilidades
        List<Map<String, Object>> habs = new ArrayList<>();
        Map<String, Object> h1 = new HashMap<>();
        h1.put("id", 1);
        h1.put("nombre", "Golpe simple");
        h1.put("descripcion", "Golpe");
        h1.put("nom_grafico", "golpesimple");
        h1.put("danoBeneficio", -5);
        h1.put("costoBasico", 0);
        h1.put("nivelMaximo", 10);
        h1.put("tiempoEspera", 2);
        habs.add(h1);

        tables.put("habilidad", habs);

        // 5. Misiones
        tables.put("mision", new ArrayList<>());

        // 6. Inventario
        tables.put("inventario", new ArrayList<>());

        // Initial save
        for (String key : tables.keySet())
            saveData(key);
    }

    private void addMob(List<Map<String, Object>> pList, List<Map<String, Object>> mobs, int id, String nombre,
            int nivel, int x, int y, int tipo, int vitalidad, int destreza, int sabiduria, int fuerza, int experiencia,
            int dinero) {
        Map<String, Object> mob = new HashMap<>();
        mob.put("personaje_id", id);
        mob.put("vitalidad", vitalidad);
        mob.put("destreza", destreza);
        mob.put("sabiduria", sabiduria);
        mob.put("fuerza", fuerza);
        mob.put("experiencia", experiencia);
        mob.put("dinero", dinero);
        // Ensure ID is present for result set
        mob.put("id", id);
        mobs.add(mob);

        Map<String, Object> p = new HashMap<>();
        p.put("id", id);
        p.put("nombre", nombre);
        p.put("nivel", nivel);
        p.put("posicionx", x);
        p.put("posiciony", y);
        p.put("tipo", tipo);
        pList.add(p);
    }

    private void addNpc(List<Map<String, Object>> pList, int id, String nombre, int nivel, int x, int y, int tipo) {
        Map<String, Object> p = new HashMap<>();
        p.put("id", id);
        p.put("nombre", nombre);
        p.put("nivel", nivel);
        p.put("posicionx", x);
        p.put("posiciony", y);
        p.put("tipo", tipo);
        pList.add(p);
    }
}
