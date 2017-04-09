package com.sergey.sample.mvvm.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Sergey Rodionov
 *         Class MaterialDialog using for creating dialogs in material design
 */

public class RossMaterialDialog extends AppCompatDialogFragment {
    public static final int BUTTON_NEGATIVE = -2;
    public static final int BUTTON_NEUTRAL = -3;
    public static final int BUTTON_POSITIVE = -1;

    protected String mText;
    protected String mTitle;
    protected String mPositiveButtonText;
    protected String mNegativeButtonText;
    protected int mTextId;
    protected int mTitleId;
    protected int mPositiveButtonTextId;
    protected int mNegativeButtonTextId;
    protected int mThemeId;
    protected int mLayoutId;
    protected int mTitleViewId;
    protected int mDescriptionViewId;
    protected int mPositiveButtonId;
    protected int mNegativeButtonId;
    protected OnClickListener mPositiveListener;
    protected OnClickListener mNegativeListener;
    protected boolean mCancelable;
    protected int mIdInList;
    protected CustomViewPreparator mCustomViewPreparator;
    protected CustomViewInflater mCustomViewInflater;

    public interface OnClickListener {
        void onClick(Activity activity, DialogInterface dialog, int buttonId, int idInList);
    }

    public interface CustomViewPreparator {
        void OnPrepare(View view, RossMaterialDialog daMaterialDialog);
    }

    public interface CustomViewInflater {
        View inflate(Context dialogContext, int layoutId);
    }

    public static class Builder<T extends Builder> {
        private String mText;
        private String mTitle;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private int mTextId;
        private int mTitleId;
        private int mPositiveButtonTextId;
        private int mNegativeButtonTextId;
        private String mTag;
        private int mThemeId;
        private int mLayoutId;
        private int mTitleViewId;
        private int mDescriptionViewId;
        private FragmentManager mFragmentManager;
        private OnClickListener mPositiveListener;
        private OnClickListener mNegativeListener;
        private int mPositiveButtonId;
        private int mNegativeButtonId;
        private boolean mCancelable;
        private int mIdInList;
        private CustomViewPreparator mCustomViewPreparator;
        private CustomViewInflater mCustomViewInflater;

        public Builder(String tag, FragmentManager fragmentManager) {
            mTag = tag;
            mThemeId = -1;
            mLayoutId = -1;
            mTitleViewId = -1;
            mDescriptionViewId = -1;
            mPositiveButtonId = -1;
            mNegativeButtonId = -1;
            mTextId = -1;
            mTitleId = -1;
            mPositiveButtonTextId = -1;
            mNegativeButtonTextId = -1;
            mFragmentManager = fragmentManager;
        }

        protected RossMaterialDialog createDialog() {
            return new RossMaterialDialog();
        }

        public void show() {
            RossMaterialDialog dialog = createDialog();
            dialog.fillFromBuilder(this);
            dialog.show(getFragmentManager(), getTag());
        }

        public T setCustomView(int layoutId, int titleViewId, int descriptionViewId, int positiveButtonId) {
            mLayoutId = layoutId;
            mTitleViewId = titleViewId;
            mDescriptionViewId = descriptionViewId;
            mPositiveButtonId = positiveButtonId;
            return (T) this;
        }

        public T setCustomView(int layoutId, int titleViewId, int descriptionViewId, int positiveButtonId, int negativeButtonId) {
            mNegativeButtonId = negativeButtonId;
            return setCustomView(layoutId, titleViewId, descriptionViewId, positiveButtonId);
        }

        public T setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return (T) this;
        }

        public T setNegativeButtonText(String negativeButtonText) {
            mNegativeButtonText = negativeButtonText;
            return (T) this;
        }

        public T setPositiveButtonText(String positiveButtonText) {
            mPositiveButtonText = positiveButtonText;
            return (T) this;
        }

        public T setNegativeButtonText(int negativeButtonTextId) {
            mNegativeButtonTextId = negativeButtonTextId;
            return (T) this;
        }

        public T setPositiveButtonText(int positiveButtonTextId) {
            mPositiveButtonTextId = positiveButtonTextId;
            return (T) this;
        }

        public T setPositiveListener(OnClickListener positiveListener) {
            mPositiveListener = positiveListener;
            return (T) this;
        }

        public T setNegativeListener(OnClickListener negativeListener) {
            mNegativeListener = negativeListener;
            return (T) this;
        }

        public T setText(String text) {
            mText = text;
            return (T) this;
        }

        public T setTitle(String title) {
            mTitle = title;
            return (T) this;
        }

        public T setText(int textId) {
            mTextId = textId;
            return (T) this;
        }

        public T setTitle(int titleId) {
            mTitleId = titleId;
            return (T) this;
        }

        public T setTheme(int themeId) {
            mThemeId = themeId;
            return (T) this;
        }

        public T fromList(int idInList) {
            mIdInList = idInList;
            return (T) this;
        }

        public T setCustomViewPreparator(CustomViewPreparator customViewPreparator) {
            mCustomViewPreparator = customViewPreparator;
            return (T) this;
        }

        public T setCustomViewInflater(CustomViewInflater customViewInflater) {
            mCustomViewInflater = customViewInflater;
            return (T) this;
        }

        protected String getText() {
            return mText;
        }

        protected String getTitle() {
            return mTitle;
        }

        protected String getTag() {
            return mTag;
        }

        protected int getThemeId() {
            return mThemeId;
        }

        protected int getLayoutId() {
            return mLayoutId;
        }

        protected int getTitleViewId() {
            return mTitleViewId;
        }

        protected int getDescriptionViewId() {
            return mDescriptionViewId;
        }

        protected FragmentManager getFragmentManager() {
            return mFragmentManager;
        }

        protected OnClickListener getPositiveListener() {
            return mPositiveListener;
        }

        protected int getPositiveButtonId() {
            return mPositiveButtonId;
        }

        protected String getPositiveButtonText() {
            return mPositiveButtonText;
        }

        protected boolean isCancelable() {
            return mCancelable;
        }

        protected String getNegativeButtonText() {
            return mNegativeButtonText;
        }

        protected OnClickListener getNegativeListener() {
            return mNegativeListener;
        }

        protected int getNegativeButtonId() {
            return mNegativeButtonId;
        }

        protected int getIdInList() {
            return mIdInList;
        }

        protected int getTextId() {
            return mTextId;
        }

        protected int getTitleId() {
            return mTitleId;
        }

        protected int getPositiveButtonTextId() {
            return mPositiveButtonTextId;
        }

        protected int getNegativeButtonTextId() {
            return mNegativeButtonTextId;
        }

        protected CustomViewPreparator getCustomViewPreparator() {
            return mCustomViewPreparator;
        }

        public CustomViewInflater getCustomViewInflater() {
            return mCustomViewInflater;
        }
    }

    public RossMaterialDialog() {
    }

    public void setCustomViewPreparator(CustomViewPreparator customViewPreparator) {
        mCustomViewPreparator = customViewPreparator;
        if (mCustomViewPreparator != null) {
            mCustomViewPreparator.OnPrepare(getView(), this);
        }
    }

    protected void fillFromBuilder(Builder builder) {
        mText = builder.getText();
        mTitle = builder.getTitle();
        mThemeId = builder.getThemeId();
        mLayoutId = builder.getLayoutId();
        mTitleViewId = builder.getTitleViewId();
        mDescriptionViewId = builder.getDescriptionViewId();
        mPositiveButtonId = builder.getPositiveButtonId();
        mNegativeButtonId = builder.getNegativeButtonId();
        mPositiveListener = builder.getPositiveListener();
        mNegativeListener = builder.getNegativeListener();
        mPositiveButtonText = builder.getPositiveButtonText();
        mNegativeButtonText = builder.getNegativeButtonText();
        mCancelable = builder.isCancelable();
        mIdInList = builder.getIdInList();
        mTextId = builder.getTextId();
        mTitleId = builder.getTitleId();
        mPositiveButtonTextId = builder.getPositiveButtonTextId();
        mNegativeButtonTextId = builder.getNegativeButtonTextId();
        mCustomViewPreparator = builder.getCustomViewPreparator();
        mCustomViewInflater = builder.getCustomViewInflater();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mThemeId", mThemeId);
        outState.putInt("mLayoutId", mLayoutId);
        outState.putInt("mTitleViewId", mTitleViewId);
        outState.putInt("mDescriptionViewId", mDescriptionViewId);
        outState.putInt("mPositiveButtonId", mPositiveButtonId);
        outState.putInt("mNegativeButtonId", mNegativeButtonId);
        outState.putInt("mIdInList", mIdInList);
        outState.putInt("mTextId", mTextId);
        outState.putInt("mTitleId", mTitleId);
        outState.putInt("mPositiveButtonTextId", mPositiveButtonTextId);
        outState.putInt("mNegativeButtonTextId", mNegativeButtonTextId);
        outState.putString("mText", mText);
        outState.putString("mTitle", mTitle);
        outState.putString("mPositiveButtonText", mPositiveButtonText);
        outState.putString("mNegativeButtonText", mNegativeButtonText);
    }

    protected void onPositive(Activity activity, DialogInterface dialog, int buttonId, int idInList) {
        if (mPositiveListener != null) {
            mPositiveListener.onClick(activity, dialog, buttonId, idInList);
        }
    }

    protected void buildCustomViewDialog(View customView, AlertDialog.Builder adb, Context dialogContext) {
        if (mTitleViewId > -1) {
            TextView titleTextView = (TextView) customView.findViewById(mTitleViewId);
            if (mTitleId > -1)
                titleTextView.setText(mTitleId);
            else
                titleTextView.setText(mTitle);
        }
        if (mDescriptionViewId > -1) {
            TextView textTextView = (TextView) customView.findViewById(mDescriptionViewId);
            if (mTextId > -1)
                textTextView.setText(mTextId);
            else
                textTextView.setText(mText);
        }

        if (mPositiveButtonId > -1) {
            Button positiveButton = (Button) customView.findViewById(mPositiveButtonId);
            if (mPositiveButtonTextId > -1) positiveButton.setText(mPositiveButtonTextId);
            if (mPositiveButtonText != null) positiveButton.setText(mPositiveButtonText);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPositive(getActivity(), getDialog(), BUTTON_POSITIVE, mIdInList);
                }
            });
        }
        if (mNegativeButtonId > -1) {
            Button negativeButton = (Button) customView.findViewById(mNegativeButtonId);
            if (mNegativeButtonTextId > -1) negativeButton.setText(mNegativeButtonTextId);
            else if (mNegativeButtonText != null) negativeButton.setText(mNegativeButtonText);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeListener != null) {
                        mNegativeListener.onClick(getActivity(), getDialog(), BUTTON_NEGATIVE, mIdInList);
                    }
                }
            });
        }
        adb.setView(customView);
    }

    protected void buildDefaultViewDialog(AlertDialog.Builder adb, Context dialogContext) {
        if (mPositiveButtonText != null || mPositiveButtonTextId > -1) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    onPositive(getActivity(), dialogInterface, i, mIdInList);
                }
            };
            if (mPositiveButtonTextId > -1) {
                adb.setPositiveButton(mPositiveButtonTextId, listener);
            } else {
                adb.setPositiveButton(mPositiveButtonText, listener);
            }
        }
        if (mNegativeButtonText != null || mNegativeButtonTextId > -1) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mNegativeListener != null) {
                        mNegativeListener.onClick(getActivity(), dialogInterface, i, mIdInList);
                    }
                }
            };
            if (mNegativeButtonTextId > -1) {
                adb.setNegativeButton(mNegativeButtonTextId, listener);
            } else {
                adb.setNegativeButton(mNegativeButtonText, listener);
            }
        }
        if (mTitleId > -1)
            adb.setTitle(mTitleId);
        else
            adb.setTitle(mTitle);

        if (mTextId > -1)
            adb.setMessage(mTextId);
        else
            adb.setMessage(mText);
    }

    protected void buildDialog(AlertDialog.Builder adb, Context dialogContext) {
        if (mLayoutId > -1) {
            View dialogView = null;
            if (mCustomViewInflater != null) {
                dialogView = mCustomViewInflater.inflate(dialogContext, mLayoutId);
            } else {
                dialogView = LayoutInflater.from(dialogContext).inflate(mLayoutId, null);
            }
            if (mCustomViewPreparator != null) {
                mCustomViewPreparator.OnPrepare(dialogView, this);
            }
            buildCustomViewDialog(dialogView, adb, dialogContext);
        } else {
            buildDefaultViewDialog(adb, dialogContext);
        }
        setCancelable(mCancelable);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (savedInstanceState != null) {
            mThemeId = savedInstanceState.getInt("mThemeId", -1);
            mLayoutId = savedInstanceState.getInt("mLayoutId", -1);
            mTitleViewId = savedInstanceState.getInt("mTitleViewId", -1);
            mDescriptionViewId = savedInstanceState.getInt("mDescriptionViewId", -1);
            mPositiveButtonId = savedInstanceState.getInt("mPositiveButtonId", -1);
            mNegativeButtonId = savedInstanceState.getInt("mNegativeButtonId", -1);
            mIdInList = savedInstanceState.getInt("mIdInList", -1);
            mTextId = savedInstanceState.getInt("mTextId", -1);
            mTitleId = savedInstanceState.getInt("mTitleId", -1);
            mPositiveButtonTextId = savedInstanceState.getInt("mPositiveButtonTextId", -1);
            mNegativeButtonTextId = savedInstanceState.getInt("mNegativeButtonTextId", -1);
            mText = savedInstanceState.getString("mText");
            mTitle = savedInstanceState.getString("mTitle");
            mPositiveButtonText = savedInstanceState.getString("mPositiveButtonText");
            mNegativeButtonText = savedInstanceState.getString("mNegativeButtonText");
        }

        Context dialogViewContext = getActivity();
        if (mThemeId > -1) {
            dialogViewContext = new ContextThemeWrapper(getActivity(), mThemeId);
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(dialogViewContext);
        buildDialog(adb, dialogViewContext);
        return adb.create();
    }
}
