/** Checked exception: the item is already on loan. */
public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String itemId, String title) {
        super("Item " + itemId + " (" + title + ") is not available");
    }
}
