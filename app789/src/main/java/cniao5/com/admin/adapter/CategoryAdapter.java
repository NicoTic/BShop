package cniao5.com.admin.adapter;

import android.content.Context;

import java.util.List;

import cniao5.com.admin.bean.Category;
import cniao5.com.cniao5shop.R;


public class CategoryAdapter extends SimpleAdapter<Category> {


    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.category_single_text_layout, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {

        viewHoder.getTextView(R.id.category_title).setText(item.getName());

    }
}
