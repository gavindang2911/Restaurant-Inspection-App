package cmpt276.sample.project.Model;

public class UpdateManager {
    private static UpdateManager updateManager = null;

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
}
