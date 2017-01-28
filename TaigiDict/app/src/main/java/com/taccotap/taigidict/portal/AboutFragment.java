package com.taccotap.taigidict.portal;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.model.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.model.MaterialAboutTitleItem;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.taccotap.taigidict.R;

public class AboutFragment extends MaterialAboutFragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        // Add items to card

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(R.string.app_name)
                .icon(R.mipmap.ic_launcher)
                .build());

        try {
            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(context,
                    new IconicsDrawable(context, CommunityMaterial.Icon.cmd_android_debug_bridge).sizeDp(48),
                    getString(R.string.about_fragment_version_item_text),
                    false));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
//                .text("Changelog")
//                .icon(context.getResources().getDrawable(R.drawable.ic_assignment_green_900_48dp))
//                .setOnClickListener(ConvenienceBuilder.createWebViewDialogOnClickAction(context, "Releases", "https://github.com/daniel-stoneuk/material-about-library/releases", true, false))
//                .build());

        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        authorCardBuilder.title(getString(R.string.about_fragment_author_card_title_text));

        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.about_fragment_author_item_text))
                .subText(getString(R.string.about_fragment_author_item_subtext))
                .icon(new IconicsDrawable(context, CommunityMaterial.Icon.cmd_account).sizeDp(48))
                .build());

        authorCardBuilder.addItem(ConvenienceBuilder.createEmailItem(context,
                new IconicsDrawable(context, CommunityMaterial.Icon.cmd_email_outline).sizeDp(48),
                getString(R.string.help_fragment_email_me_item_text),
                true,
                getString(R.string.feedback_email),
                getString(R.string.help_fragment_email_me_mail_title)));

        return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build());
    }
}
