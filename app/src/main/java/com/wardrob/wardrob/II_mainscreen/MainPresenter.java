package com.wardrob.wardrob.II_mainscreen;


import com.wardrob.wardrob.R;
import com.wardrob.wardrob.core.ResourcesGetterSingleton;

public class MainPresenter
{

    MainView view = null;

    public MainPresenter(MainView view)
    {
        this.view = view;
    }


    /**
     * Function: getFragmentFolderPath
     * @param position
     * @return path String
     */
    public String getFragmentFolderPath(Integer position)
    {
        String path = "";

        switch(position)
        {
            case 0:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                                ResourcesGetterSingleton.getStr(R.string.path_upper_outfit);
                break;

            case 1:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_lower_outfit);
                break;

            case 2:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_outerwear);
                break;

            case 3:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                    ResourcesGetterSingleton.getStr(R.string.path_hats);
                break;

            case 4:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_shoes);
                break;

            case 5:
                path = ResourcesGetterSingleton.getStr(R.string.path_main) +
                        ResourcesGetterSingleton.getStr(R.string.path_accessories);
                break;

            default:
                break;
        }
        return path;
    }

}
