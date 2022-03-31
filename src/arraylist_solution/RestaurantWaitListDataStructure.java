package arraylist_solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*

Implement a restaurant waitlist data structure. It should support the following features:
1. A party of customers can join the waitlist.
2. A previously joined party can leave the waitlist at any time.
3. The restaurant serves teh first party whose size fits the empty table size at a time ( a table size
is given as an argument)

-> correct solution
-> run time complexity also matters
 */
public class RestaurantWaitListDataStructure {
    private List<Party> waitList;

    public RestaurantWaitListDataStructure() {
        this.waitList = new ArrayList<>();
    }

// O(waitList)  ~ 2 * waitlist <-- approximate number of steps
    void leave(Party party) {
        // search this party with the id in the List and remove it.
        int index = -1;
        for (int i = 0; i < waitList.size(); i++) {
            if (waitList.get(i).getId().equals(party.getId())) {
                index = i;
                break;
            }
        }
        if (index != -1)
            waitList.remove(index);
    }
// O(1)
    void joinTheWaitList(Party party) {
        waitList.add(party);
    }
// // O(waitList)  ~ 3 * waitlist <-- approximate number of steps
    Party serve(Integer tableSize) {
        Party party = null;
        for (int i = 0; i < waitList.size(); i++) {
            if (waitList.get(i).getCustomers().size() <= tableSize) {
                party = waitList.get(i);
                break;
            }
        }
        if (party != null)
            leave(party);
        // loop to remove this party from the list
        return party;
    }

}

class Party {
    private String id;
    private Set<String> customers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<String> customers) {
        this.customers = customers;
    }

}
