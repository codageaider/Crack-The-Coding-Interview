package linked_hashmap;

import java.util.LinkedHashMap;
import java.util.Map;
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
public class RestaurantWaitListDSwithHashMap {
    private Map<String, Party> waitMap;

    public RestaurantWaitListDSwithHashMap() {
        this.waitMap = new LinkedHashMap<>();
    }

    // O(1) average time complexity
    void leave(Party party) {
        waitMap.remove(party.getId());
    }

    // O(1)
    void joinTheWaitList(Party party) {
        waitMap.put(party.getId(), party);
    }

    // O(waitlist) steps
    Party serve(Integer tableSize) {
        Party partyToBeServed = null;
        for (String key : waitMap.keySet()) {
            Party party = waitMap.get(key);
            if (party.getCustomers().size() <= tableSize) {
                partyToBeServed = party;
                break;
            }
        }
        if (partyToBeServed == null)
            return null;
        // O(1) time complexity
        leave(partyToBeServed);
        return partyToBeServed;
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
