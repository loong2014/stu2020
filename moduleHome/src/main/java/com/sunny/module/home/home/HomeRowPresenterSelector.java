package com.sunny.module.home.home;

import com.sunny.module.home.home.presenter.HomeChannelLayoutPresenter;
import com.sunny.module.home.home.presenter.HomeHeadLayoutPresenter;
import com.sunny.module.home.template.ItemEntity;
import com.sunny.module.home.template.Presenter;
import com.sunny.module.home.template.selector.PresenterSelector;

import java.util.HashMap;
import java.util.Map;

public class HomeRowPresenterSelector extends PresenterSelector {

    private final Map<Integer, Presenter> presenterMap = new HashMap<>();


    @Override
    public Presenter getPresenter(Object item) {
        if (!(item instanceof ItemEntity)) throw new RuntimeException(
                String.format("The PresenterSelector only supports data items of type '%s'",
                        ItemEntity.class.getName()));

        ItemEntity itemEntity = (ItemEntity) item;

        Presenter presenter = presenterMap.get(itemEntity.getItemType());
        if (presenter == null) {

            switch (itemEntity.getItemType()) {

                case HomeTemplate.RowType.TYPE_HEAD:
                    presenter = new HomeHeadLayoutPresenter();
                    break;

                case HomeTemplate.RowType.TYPE_CHANNEL:
                    presenter = new HomeChannelLayoutPresenter();
                    break;

                default:
                    break;
            }

            presenterMap.put(itemEntity.getItemType(), presenter);
        }

        return presenter;
    }
}
