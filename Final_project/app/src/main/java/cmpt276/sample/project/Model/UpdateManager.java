package cmpt276.sample.project.Model;

/**
 * Class for Update object, that contain variables and methods to hold the last update time
 * and check if we need to update or not.
 */
public class UpdateManager {
    private static UpdateManager updateManager = null;
    private String lastModifiedForInspections;
    private String lastModifiedForRestaurants;

    public static UpdateManager getInstance(){
        if(updateManager == null){
            updateManager = new UpdateManager();
        }
        return updateManager;
    }

    public void setLastModifiedRestaurantsFirstTime(String last_modified) {
    }

    public void setLastModifiedRestaurants(String last_modified) {
    }

    public void setLastModifiedInspectionsFirstTime(String last_modified_inspection) {
    }

    public void setLastModifiedInspections(String last_modified_inspection) {
    }

    public void setLastUpdate(String todayString) {
    }
}
