package site.cpsp.myledger.utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by WINDOWS7 on 2018-08-15.
 */
public class SimpleDialogUtil extends DialogFragment {
    public interface Callback{
        void callbackMethod();
    }

    private String message;
    private String positiveMsg;
    private String negativeMsg;
    private Callback callback_positive=null;
    private Callback callback_negative=null;
    private View childView=null;

    private String list[]=null;
    private DialogInterface.OnClickListener adapterListener=null;


    //Before call show() of instance of this class, must call this method.
    public void setValue(String message, String positiveMsg, String negativeMsg, Callback callback_positive, Callback callback_negative){
        this.message=message;
        this.positiveMsg=positiveMsg;
        this.negativeMsg=negativeMsg;
        this.callback_positive=callback_positive;
        this.callback_negative=callback_negative;
    }
    public void setValue(String message, String positiveMsg, String negativeMsg, Callback callback_positive, Callback callback_negative, View childView){
        this.message=message;
        this.positiveMsg=positiveMsg;
        this.negativeMsg=negativeMsg;
        this.callback_positive=callback_positive;
        this.callback_negative=callback_negative;
        this.childView=childView;
    }
    public void setValue(View childView){
        this.childView= childView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        if(childView!=null){
            builder.setView(childView);
        }

        if(list!=null){
            builder.setItems(list, adapterListener);
        }
        // Create the AlertDialog object and return it
        final AlertDialog dialog=builder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
                //Prevent to close dialog when positive button is clicked.
                if(callback_positive!=null){
                    callback_positive.callbackMethod();
                }else{
                    dialog.dismiss();
                }
            });
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
                //Prevent to close dialog when negative button is clicked.
                if(callback_negative!=null){
                    callback_negative.callbackMethod();
                }else{
                    dialog.dismiss();
                }
            });
        });
        return dialog;
    }

    public static ProgressDialog showProgressDialog(Activity activity, String title, String message){
        ProgressDialog dialog = ProgressDialog.show(activity, title, message, true);
        return dialog;
    }
}