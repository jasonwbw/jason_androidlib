package personal.jason.androidlib.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 多选Preference，可继承方便的获取参数值
 * 
 * @author Jason_wbw
 *
 */

public class MutiSelectListPreference extends ListPreference {

    private static final String SEPARATOR = "";
    private static final String LOGTAG = "MutiSelectListPreference";
    
    protected boolean[] mClickedDialogEntryIndices;
    
    public MutiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mClickedDialogEntryIndices = new boolean[getEntries().length];
    }
    @Override
    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        mClickedDialogEntryIndices = new boolean[entries.length];
    }

    public MutiSelectListPreference(Context context) {
        this(context, null);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // TODO Auto-generated method stub
        CharSequence[] entryValues = getEntryValues();
        if (positiveResult && entryValues != null) {
            StringBuffer value = new StringBuffer();
            for (int i = 0; i < entryValues.length; i++) {
                if (mClickedDialogEntryIndices[i]) {
                	value.append(entryValues[i]).append(SEPARATOR);
                }
            }

            if (callChangeListener(value)) {
                String val = value.toString();
                Log.d(LOGTAG, "val:"+val);
                if (val.length() > 0)
                    val = val.substring(0, val.length() - SEPARATOR.length());
                setValue(val);
            }
        }
    
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        // TODO Auto-generated method stub
        CharSequence[] entries = getEntries();
        CharSequence[] entryValues = getEntryValues();
        Log.d(LOGTAG , "onPrepareDialogBuilder entries = "+ entries.toString() + " entryValues = " + entryValues.toString());
        if (entries == null || entryValues == null
                || entries.length != entryValues.length) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array which are both the same length");
        }

        restoreCheckedEntries();
        builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
                new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
                            boolean val) {
                        Log.d(LOGTAG, "OnMultiChoiceClickListener val = "+ val);
                        mClickedDialogEntryIndices[which] = val;
                    }
                });
    }

    
    public static String[] parseStoredValue(CharSequence val) {
        Log.d(LOGTAG, "parseStoredValue val = " + val.toString());
        if ("".equals(val))
            return null;
        else
            return ((String) val).split(SEPARATOR);
    }

    private void restoreCheckedEntries() {
        CharSequence[] entryValues = getEntryValues();

        String[] vals = parseStoredValue(getValue());
        
        if (vals != null) {
            Log.d(LOGTAG, "restoreCheckedEntries vals = " + vals);
            for (int j = 0; j < vals.length; j++) {
                Log.d(LOGTAG, "restoreCheckedEntries val without trim = " + vals[j]);
                String val = vals[j].trim();
                Log.d(LOGTAG, "restoreCheckedEntries val = " + val);
                for (int i = 0; i < entryValues.length; i++) {
                    CharSequence entry = entryValues[i];
                    if (entry.equals(val)) {
                        mClickedDialogEntryIndices[i] = true;
                        break;
                    }
                }
            }
        }
    }
}
