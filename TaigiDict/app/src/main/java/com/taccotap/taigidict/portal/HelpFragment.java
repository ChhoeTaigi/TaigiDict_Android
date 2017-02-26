package com.taccotap.taigidict.portal;

import android.content.Context;
import android.net.Uri;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.model.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.model.MaterialAboutTitleItem;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.taccotap.taigidict.R;

public class HelpFragment extends MaterialAboutFragment {

    public HelpFragment() {
        // Required empty public constructor
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

        // Add items to card

        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(getString(R.string.help_fragment_title_card_text))
                .icon(context.getResources().getDrawable(R.drawable.ic_favorite_green_900_48dp))
                .build());

        MaterialAboutCard.Builder feedbackCardBuilder = new MaterialAboutCard.Builder();
        feedbackCardBuilder.title(getString(R.string.help_fragment_feedback_card_title_text));

        feedbackCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(context,
                getResources().getDrawable(R.drawable.line_app_icon),
                getString(R.string.help_fragment_line_account_item_text),
                true,
                Uri.parse(getString(R.string.url_taigidictapp_line_account))));

        feedbackCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(context,
                new IconicsDrawable(context, CommunityMaterial.Icon.cmd_facebook_box).sizeDp(48),
                getString(R.string.help_fragment_facebook_group_item_text),
                true,
                Uri.parse(getString(R.string.url_taigidictapp_facebook_group))));

        feedbackCardBuilder.addItem(ConvenienceBuilder.createEmailItem(context,
                new IconicsDrawable(context, CommunityMaterial.Icon.cmd_email_outline).sizeDp(48),
                getString(R.string.help_fragment_email_me_item_text),
                true,
                getString(R.string.feedback_email),
                getString(R.string.help_fragment_email_me_mail_title)));

        MaterialAboutCard.Builder helpCardBuilder = new MaterialAboutCard.Builder();
        helpCardBuilder.title(getString(R.string.help_fragment_help_card_title_text));

        helpCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(context,
                new IconicsDrawable(context, CommunityMaterial.Icon.cmd_star).sizeDp(48),
                getString(R.string.help_fragment_rate_app_item_text),
                null
        ));

        helpCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Fork on GitHub")
                .icon(new IconicsDrawable(context, CommunityMaterial.Icon.cmd_github_circle).sizeDp(48))
                .setOnClickListener(ConvenienceBuilder.createWebsiteOnClickAction(context, Uri.parse(getString(R.string.url_taigidictapp_github_repo))))
                .build());

        return new MaterialAboutList(appCardBuilder.build(), feedbackCardBuilder.build(), helpCardBuilder.build());
    }
}
