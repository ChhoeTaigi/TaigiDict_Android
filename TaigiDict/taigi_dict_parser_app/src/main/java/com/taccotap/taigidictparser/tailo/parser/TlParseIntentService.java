package com.taccotap.taigidictparser.tailo.parser;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.taccotap.taigidictmodel.tailo.TlDescription;
import com.taccotap.taigidictmodel.tailo.TlHoagiWord;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;
import com.taccotap.taigidictparser.utils.ExcelUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;

public class TlParseIntentService extends IntentService {
    private static final String TAG = TlParseIntentService.class.getSimpleName();

    private static final String ASSETS_PATH_TAILO_DICT_TAIGI_WORDS = "tailo/詞目總檔(含俗諺).xls";
    private static final String ASSETS_PATH_TAILO_DICT_HOAGI_WORDS = "tailo/對應華語(不公開項目).xls";
    private static final String ASSETS_PATH_TAILO_DICT_DESCRIPTIONS = "tailo/釋義.xls";

    private Realm mRealm;

    public TlParseIntentService() {
        super("TlParseIntentService");
    }

    public static void startParsing(Context context) {
        Intent intent = new Intent(context, TlParseIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        parseDict();
    }

    private void parseDict() {
        mRealm = Realm.getDefaultInstance();

        parseDictTaigiWord();
        parseDictHoagiWord();
        parseDictDescription();

        mRealm.close();
    }

    private void parseDictTaigiWord() {
        Log.d(TAG, "start: parseDictTaigiWord()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_TAIGI_WORDS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlTaigiWord> taigiWords = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlTaigiWord currentTaigiWord = new TlTaigiWord();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentTaigiWord.mainCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentTaigiWord.propertyCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 3) {
                        currentTaigiWord.hanji = currentCell.getStringCellValue();
                    } else if (colNum == 4) {
                        currentTaigiWord.lomaji = currentCell.getStringCellValue();
                    } else {
                        break;
                    }

                    colNum++;
                }

                taigiWords.add(currentTaigiWord);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlTaigiWord taigiWord : taigiWords) {
                        realm.copyToRealmOrUpdate(taigiWord);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictTaigiWord()");
    }

    private void parseDictHoagiWord() {
        Log.d(TAG, "start: parseDictHoagiWord()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_HOAGI_WORDS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlHoagiWord> hoagiWords = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlHoagiWord currentHoagiWord = new TlHoagiWord();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentHoagiWord.hoagiCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentHoagiWord.mainCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 3) {
                        currentHoagiWord.hoagiWord = currentCell.getStringCellValue();
                    } else {
                        break;
                    }

                    colNum++;
                }

                hoagiWords.add(currentHoagiWord);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlHoagiWord hoagiWord : hoagiWords) {
                        realm.copyToRealmOrUpdate(hoagiWord);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictHoagiWord()");
    }

    private void parseDictDescription() {
        Log.d(TAG, "start: parseDictDescription()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_DESCRIPTIONS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlDescription> descriptions = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlDescription currentDescription = new TlDescription();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.descCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.mainCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 3) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.descOrder = Integer.valueOf(stringCellValue);
                    } else if (colNum == 4) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.wordTypeCode = Integer.valueOf(stringCellValue);
                    } else if (colNum == 5) {
                        currentDescription.description = currentCell.getStringCellValue();
                    } else {
                        break;
                    }

                    colNum++;
                }

                descriptions.add(currentDescription);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlDescription description : descriptions) {
                        realm.copyToRealmOrUpdate(description);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictDescription()");
    }
}
