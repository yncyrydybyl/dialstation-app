/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.telekommunisten.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.telekommunisten.Constants;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter contacts to the
 * platform ContactOperations provider.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    private final AccountManager mAccountManager;
    private final Context mContext;

    private Date mLastUpdated;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
        ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "performing dialstation SYNC");
        
        Cursor pdns = getContext().getContentResolver().query(Uri.parse("content://com.dialstation"), null, null, null, null);
        
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            while (pdns.moveToNext()) {
                
                // check if this contact is already stored offline in ContactsProvider
                Cursor c = getContext().getContentResolver().query(RawContacts.CONTENT_URI,
                        new String[]{RawContacts._ID}, 
                        RawContacts.SOURCE_ID + "=?",
                        new String[]{pdns.getString(pdns.getColumnIndex("id"))}, null);
                // if not
                if (c.getCount() == 0) {
                    // insert new contact
                    int rawContactInsertIndex = ops.size();
                    ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                            .withValue(RawContacts.ACCOUNT_TYPE, account.type)
                            .withValue(RawContacts.ACCOUNT_NAME, account.name)
                            .withValue(RawContacts.SOURCE_ID, pdns.getString(pdns.getColumnIndex("id")))
                            .build());
                    ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                            .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                            .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(StructuredName.DISPLAY_NAME, pdns.getString(pdns.getColumnIndex("description")))
                            .build());
                    ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                            .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                            .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                            .withValue(Phone.NUMBER, pdns.getString(pdns.getColumnIndex("destination")))
                            .build());
                    ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                            .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                            .withValue(Data.MIMETYPE, Constants.MIMETYPE)
                            .withValue(Data.DATA1, pdns.getString(pdns.getColumnIndex("pstn_number")))
                            .withValue(Data.DATA2, "23 ct/min  (:-P telco!)")
                            .withValue(Data.DATA3, "pdn: "+pdns.getString(pdns.getColumnIndex("id")))
                            .build());
                }
            }
            getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
}
