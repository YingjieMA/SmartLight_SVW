package com.csvw.myj.smartlight;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by LiBin on 2016/6/16.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private Drawable positiveButtonView;
        private Drawable negativeButtonView;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param imageView
         * @return
         */
        public Builder setPositiveButton(Drawable imageView,
                                         OnClickListener listener) {
            this.positiveButtonView = imageView;
            this.positiveButtonClickListener = listener;
            return this;
        }



        public Builder setNegativeButton(Drawable imageView,
                                         OnClickListener listener) {
            this.negativeButtonView = imageView;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_connect, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonView != null) {
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.positiveTextView)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveTextView).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonView != null) {
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.negativeTextView)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeTextView).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
