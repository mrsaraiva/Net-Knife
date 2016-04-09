package matrix.the.net_knife.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import matrix.the.net_knife.R;

import java.util.List;

/**
 * Created by JoaoLuiz on 19/03/2016.
 */
public class AdapterListView extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<ItemListView> itens;

    public AdapterListView(Context context, List<ItemListView> itens)
    {
        //Itens do listview
        this.itens = itens;
        // Objeto responsável por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return itens.size();
    }

    public ItemListView getItem(int position)
    {
        return itens.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (view == null)
        {
            //infla o layout para podermos pegar as views
            view = mInflater.inflate(R.layout.layout_item_main_list_view, null);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();
            itemHolder.txtTitle = ((TextView) view.findViewById(R.id.text));

            System.out.println("CARREGANDO POSICAO " + position);

            //define os itens na view;
            view.setTag(itemHolder);
        }
        else
        {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) view.getTag();
        }

        //pega os dados da lista
        //e define os valores nos itens.
        ItemListView item = itens.get(position);
        itemHolder.txtTitle.setText(item.getTexto());
        itemHolder.iconCheck.setTypeface(Typeface.createFromAsset(view.getContext().getAssets(), "fontawesome-webfont.ttf"));

        //retorna a view com as informações
        return view;
    }

    /** * Classe de suporte para os itens do layout. */
    private class ItemSuporte
    {
        TextView txtTitle;
        TextView iconCheck;
    }

}
