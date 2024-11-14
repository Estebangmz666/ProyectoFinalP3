package co.edu.uniquindio.banco.bancouq.utils;

import java.nio.file.Paths;

public class PathSolver {

    private static final String resourcePath = Paths.get("src/main/resources").toAbsolutePath().toString();


    /**
     * <b>Metodo estatico el cual busca recursos en el directorio base de "resources"</b>
     * @param resourceName
     * @return String
     */
    public static String getResourcePath(String resourceName){
        String recurso = Paths.get(resourcePath, resourceName).toAbsolutePath().toString();
        return recurso;
    }

}
