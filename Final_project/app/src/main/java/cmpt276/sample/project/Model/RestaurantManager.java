package cmpt276.sample.project.Model;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RestaurantManager{
    private List<Restaurant> restaurantList = new ArrayList<>();
    private int account;

    private RestaurantManager(){}

    public void add(Restaurant restaurant){
        restaurantList.add(restaurant);
        account++;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    private static RestaurantManager restaurantManager = null;
    public static RestaurantManager getInstance(){
        if(restaurantManager==null){
            restaurantManager = new RestaurantManager();
        }
        return restaurantManager;
    }

    public Restaurant getRestaurant(int n){return restaurantList.get(n);}
    public int getAccount(){return account;}
}
