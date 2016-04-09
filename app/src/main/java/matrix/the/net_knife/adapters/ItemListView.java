package matrix.the.net_knife.adapters;

/**
 * Created by JoaoLuiz on 19/03/2016.
 */
public class ItemListView {

    private String texto;

    public ItemListView()
    {
        this("");
    }

    public ItemListView(String texto)
    {
        this.texto = texto;
    }

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }

}
