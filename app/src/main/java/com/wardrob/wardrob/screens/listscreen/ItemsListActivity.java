package com.wardrob.wardrob.screens.listscreen;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;
import com.wardrob.wardrob.screens.fragments.fragment_itemslist.FragmentItemsList;
import com.wardrob.wardrob.screens.new_item.NewItemActivity;

import java.util.HashMap;

import timber.log.Timber;

public class ItemsListActivity extends AppCompatActivity implements ItemsListView
{

    private final Integer CONST_UPPER       = 0;
    private final Integer CONST_LOWER       = 1;
    private final Integer CONST_WARM        = 2;
    private final Integer CONST_HAT         = 3;
    private final Integer CONST_BOOTS       = 4;
    private final Integer CONST_ACCESSORIES = 5;

    private final Integer CONST_LOOK        = 6;
    private final Integer CONST_WISHLIST    = 7;

    ItemsListPresenter presenter;

    public String STATE;
    public String CATEGORY;

    public FragmentItemsList fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        presenter = new ItemsListPresenter(this);

        // --------------------------------------------------------------------------------------

        Intent intent = this.getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)
                intent.getSerializableExtra(
                        ResourcesGetterSingleton.getStr(R.string.bundle_hash));

        STATE    = hashMap.get(ResourcesGetterSingleton.getStr(R.string.bundle_state));
        CATEGORY = hashMap.get(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index));

        // --------------------------------------------------------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_list);

        if (STATE.equals(ResourcesGetterSingleton.getStr(R.string.state_pick)))
        {
            fab.setVisibility(View.GONE);
        }
        else
        {
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getThis(), NewItemActivity.class);
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item),
                            presenter.getFragmentFolderPath(CATEGORY));

                    hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_new_item_index),
                            String.valueOf(presenter.getFragmentIndex(CATEGORY)));
                    hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_state),
                            STATE);

                    hashMap.put(ResourcesGetterSingleton.getStr(R.string.bundle_id_item), "none");

                    intent.putExtra(ResourcesGetterSingleton.getStr(R.string.bundle_hash), hashMap);
                    ItemsListActivity.this.startActivity(intent);
                }
            });
        }
        // --------------------------------------------------------------------------------------

        fragment = new FragmentItemsList();
        Bundle args = new Bundle();

        args.putInt(FragmentItemsList.IMAGE_RESOURCE_ID,
                    R.drawable.shirt); //TODO: send correct image id to fragment here
        args.putString(FragmentItemsList.ITEM_NAME,  CATEGORY);
        args.putString(FragmentItemsList.ITEM_STATE, STATE);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                        .replace(R.id.items_list_container, fragment)
                        .commit();

        // --------------------------------------------------------------------------------------

    }

    @Override
    public Context getThis() {
        return this;
    }
}
