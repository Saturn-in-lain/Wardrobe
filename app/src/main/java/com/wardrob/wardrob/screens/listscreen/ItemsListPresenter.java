package com.wardrob.wardrob.screens.listscreen;

import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;


public class ItemsListPresenter
{
    ItemsListView view;

    public ItemsListPresenter(ItemsListView v)
    {
        this.view = v;
    }


    /**
     * Function: getFragmentFolderPath
     * @param category
     * @return path String
     */
    public String getFragmentFolderPath(String category)
    {
        String path = "";

        switch(category)
        {
            case "Upper Level":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_upper_outfit);
                break;

            case "Lower level":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_lower_outfit);
                break;

            case "Warm Clothes":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_outerwear);
                break;

            case "Hats":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_hats);
                break;

            case "Boots":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_shoes);
                break;

            case "Accessories":
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_accessories);
                break;

            default:
                break;
        }
        return path;
    }

    /**
     * Function: getFragmentFolderPath
     * @param category
     * @return path String
     */
    public Integer getFragmentIndex(String category)
    {
        Integer index = null;

        switch(category)
        {
            case "Upper Level":
                index = 0;
                break;

            case "Lower level":
                index = 1;
                break;

            case "Warm Clothes":
                index = 2;
                break;

            case "Hats":
                index = 3;
                break;

            case "Boots":
                index = 4;
                break;

            case "Accessories":
                index = 5;
                break;

            default:
                break;
        }
        return index;
    }
}
