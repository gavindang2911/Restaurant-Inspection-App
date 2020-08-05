package cmpt276.sample.project.Model;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * RestaurantManager class which holds the array list of all restaurants extract from CSV file
 *
 * @author Gavin Dang, ttd6
 * @author Lu Xi Wang, lxwang
 * @author Shan Qing, sqing
 */
public class RestaurantManager implements Iterable<Restaurant>{
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

    public void reset() {
        restaurantManager = null;
    }
    public Restaurant getRestaurant(int n){return restaurantList.get(n);}
    public Restaurant getRestaurant(String trackingNumber){
        for(Restaurant res : restaurantManager){
            if(res.getTrackingNumber().equals(trackingNumber)){
                return res;
            }
        }
        return null;
    }
    public int getAccount(){return account;}

    public List<Restaurant> getRestaurantList() { return restaurantList; }

    @Override
    public Iterator<Restaurant> iterator() {
        return restaurantList.iterator();
    }
}
