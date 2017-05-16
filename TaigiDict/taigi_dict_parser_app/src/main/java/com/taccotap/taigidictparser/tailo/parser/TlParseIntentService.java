package com.taccotap.taigidictparser.tailo.parser;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

import com.taccotap.taigidictmodel.tailo.TlAnotherPronounce;
import com.taccotap.taigidictmodel.tailo.TlAntonym;
import com.taccotap.taigidictmodel.tailo.TlDescription;
import com.taccotap.taigidictmodel.tailo.TlDescriptionPartOfSpeech;
import com.taccotap.taigidictmodel.tailo.TlExampleSentence;
import com.taccotap.taigidictmodel.tailo.TlHoagiWord;
import com.taccotap.taigidictmodel.tailo.TlTaigiWord;
import com.taccotap.taigidictmodel.tailo.TlTaigiWordProperty;
import com.taccotap.taigidictmodel.tailo.TlThesaurus;
import com.taccotap.taigidictparser.utils.ExcelUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class TlParseIntentService extends IntentService {
    private static final String TAG = TlParseIntentService.class.getSimpleName();

    private static final String ASSETS_PATH_TAILO_DICT_TAIGI_WORDS = "tailo/詞目總檔(含俗諺).xls";
    private static final String ASSETS_PATH_TAILO_DICT_HOAGI_WORDS = "tailo/華語對應.xls";
    private static final String ASSETS_PATH_TAILO_DICT_DESCRIPTIONS = "tailo/釋義.xls";
    private static final String ASSETS_PATH_TAILO_DICT_EXAMPLE_SENTENCES = "tailo/例句.xls";
    private static final String ASSETS_PATH_TAILO_DICT_ANOTHER_PRONOUNCE = "tailo/又音(又唸作).xls";
    private static final String ASSETS_PATH_TAILO_DICT_THESAURUS = "tailo/近義詞對應.xls";
    private static final String ASSETS_PATH_TAILO_DICT_ANTONYM = "tailo/反義詞對應.xls";

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
        // worker thread
        parseDict();
    }

    private void parseDict() {
        mRealm = Realm.getDefaultInstance();

        parseDictTaigiWord();
        parseDictTaigiWordProperty();

        parseDictHoagiWord();

        parseDictDescription();
        parseDictDescriptionPartOfSpeech();

        parseExampleSentence();

        parseAnotherPronounce();

        parseThesaurus();
        parseAnyonym();

        mRealm.close();

        Log.d(TAG, "finish ALL");
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
                        currentTaigiWord.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentTaigiWord.setWordPropertyCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        currentTaigiWord.setHanji(currentCell.getStringCellValue());
                    } else if (colNum == 4) {
                        currentTaigiWord.setLomaji(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    currentTaigiWord.setDescriptions(new RealmList<TlDescription>());
                    currentTaigiWord.setHoagiWords(new RealmList<TlHoagiWord>());

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

    private void parseDictTaigiWordProperty() {
        Log.d(TAG, "start: parseDictTaigiWordProperty()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_TAIGI_WORDS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(1);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlTaigiWordProperty> taigiWordProperties = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlTaigiWordProperty currentTaigiWordProperty = new TlTaigiWordProperty();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        currentTaigiWordProperty.setPropertyCode((int) currentCell.getNumericCellValue());
                    } else if (colNum == 2) {
                        currentTaigiWordProperty.setPropertyText(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    colNum++;
                }

                taigiWordProperties.add(currentTaigiWordProperty);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlTaigiWordProperty currentTaigiWordProperty : taigiWordProperties) {
                        realm.copyToRealmOrUpdate(currentTaigiWordProperty);
                    }
                }
            });

            // add to TaigiWord
            final SparseArray<String> wordPropertySparseArray = new SparseArray<>();
            for (final TlTaigiWordProperty wordProperty : taigiWordProperties) {
                wordPropertySparseArray.put(wordProperty.getPropertyCode(), wordProperty.getPropertyText());
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmResults<TlTaigiWord> taigiWords = mRealm.where(TlTaigiWord.class).findAll();
                    for (final TlTaigiWord taigiWord : taigiWords) {
                        final String wordPropertyText = wordPropertySparseArray.get(taigiWord.getWordPropertyCode());
                        if (wordPropertyText == null) {
                            Log.e(TAG, "TlTaigiWord for TlTaigiWordProperty's wordPropertyText with propertyCode = " + taigiWord.getWordPropertyCode() + " not found.");
                            continue;
                        }
                        taigiWord.setWordPropertyText(wordPropertyText);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictTaigiWordProperty()");
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
                        currentHoagiWord.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        currentHoagiWord.setHoagiWord(currentCell.getStringCellValue());
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

            // add to TaigiWord
            final RealmResults<TlTaigiWord> taigiWords = mRealm.where(TlTaigiWord.class).findAll();
            final SparseArray<TlTaigiWord> taigiWordSparseArray = new SparseArray<>();
            for (final TlTaigiWord taigiWord : taigiWords) {
                taigiWordSparseArray.put(taigiWord.getMainCode(), taigiWord);
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (final TlHoagiWord hoagiWord : hoagiWords) {
                        final int mainCode = hoagiWord.getMainCode();
                        final TlTaigiWord foundTaigiWord = taigiWordSparseArray.get(mainCode);
                        if (foundTaigiWord == null) {
                            Log.e(TAG, "TlTaigiWord for TlHoagiWord's maincode = " + mainCode + " not found.");
                            continue;
                        }
                        foundTaigiWord.getHoagiWords().add(hoagiWord);
                    }
                }
            });

            Log.d(TAG, "finish: parseDictHoagiWord()");
        }
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
                        currentDescription.setDescCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.setDescOrder(Integer.valueOf(stringCellValue));
                    } else if (colNum == 4) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentDescription.setPartOfSpeechCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 5) {
                        currentDescription.setDescription(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    currentDescription.setExampleSentences(new RealmList<TlExampleSentence>());

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

            // add to TaigiWord
            final RealmResults<TlTaigiWord> taigiWords = mRealm.where(TlTaigiWord.class).findAll();
            final SparseArray<TlTaigiWord> taigiWordSparseArray = new SparseArray<>();
            for (final TlTaigiWord taigiWord : taigiWords) {
                taigiWordSparseArray.put(taigiWord.getMainCode(), taigiWord);
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (final TlDescription description : descriptions) {
                        final int mainCode = description.getMainCode();
                        final TlTaigiWord foundTaigiWord = taigiWordSparseArray.get(mainCode);
                        if (foundTaigiWord == null) {
                            Log.e(TAG, "TlTaigiWord for TlDescription's maincode = " + mainCode + " not found.");
                            continue;
                        }
                        foundTaigiWord.getDescriptions().add(description);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictDescription()");
    }

    // 詞性
    private void parseDictDescriptionPartOfSpeech() {
        Log.d(TAG, "start: parseDictDescriptionPartOfSpeech()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_DESCRIPTIONS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(1);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlDescriptionPartOfSpeech> descriptionPartOfSpeeches = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlDescriptionPartOfSpeech currentDescriptionPartOfSpeech = new TlDescriptionPartOfSpeech();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        currentDescriptionPartOfSpeech.setPartOfSpeechCode((int) currentCell.getNumericCellValue());
                    } else if (colNum == 2) {
                        currentDescriptionPartOfSpeech.setPartOfSpeechText(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    colNum++;
                }

                descriptionPartOfSpeeches.add(currentDescriptionPartOfSpeech);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlDescriptionPartOfSpeech currentDescriptionPartOfSpeech : descriptionPartOfSpeeches) {
                        realm.copyToRealmOrUpdate(currentDescriptionPartOfSpeech);
                    }
                }
            });

            // add to TlDescription
            final SparseArray<String> partOfSpeechSparseArray = new SparseArray<>();
            for (final TlDescriptionPartOfSpeech partOfSpeech : descriptionPartOfSpeeches) {
                partOfSpeechSparseArray.put(partOfSpeech.getPartOfSpeechCode(), partOfSpeech.getPartOfSpeechText());
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmResults<TlDescription> descriptions = mRealm.where(TlDescription.class).findAll();
                    for (final TlDescription description : descriptions) {
                        final String partOfSpeechText = partOfSpeechSparseArray.get(description.getPartOfSpeechCode());
                        if (partOfSpeechText == null) {
                            Log.e(TAG, "TlDescription for TlDescriptionPartOfSpeech's partOfSpeechText with PartOfSpeechCode = " + description.getPartOfSpeechCode() + " not found.");
                            continue;
                        }
                        description.setPartOfSpeech(partOfSpeechText);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseDictDescriptionPartOfSpeech()");
    }

    private void parseAnotherPronounce() {
        Log.d(TAG, "start: parseAnotherPronounce()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_ANOTHER_PRONOUNCE);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlAnotherPronounce> anotherPronounces = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlAnotherPronounce currentAnotherPronounce = new TlAnotherPronounce();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAnotherPronounce.setAnotherPronounceCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAnotherPronounce.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        currentAnotherPronounce.setAnotherPronounceLomaji(currentCell.getStringCellValue());
                    } else if (colNum == 4) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAnotherPronounce.setAnotherPronounceProperty(Integer.valueOf(stringCellValue));
                    } else {
                        break;
                    }

                    colNum++;
                }

                anotherPronounces.add(currentAnotherPronounce);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlAnotherPronounce currentAnotherPronounce : anotherPronounces) {
                        realm.copyToRealmOrUpdate(currentAnotherPronounce);
                    }
                }
            });

        }

        Log.d(TAG, "finish: parseAnotherPronounce()");
    }

    private void parseExampleSentence() {
        Log.d(TAG, "start: parseExampleSentence()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_EXAMPLE_SENTENCES);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlExampleSentence> exampleSentences = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlExampleSentence currentExampleSentence = new TlExampleSentence();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentExampleSentence.setExampleSentenceCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentExampleSentence.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentExampleSentence.setDescCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 4) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentExampleSentence.setExampleSentenceOrder(Integer.valueOf(stringCellValue));
                    } else if (colNum == 5) {
                        currentExampleSentence.setExampleSentenceHanji(currentCell.getStringCellValue());
                    } else if (colNum == 6) {
                        currentExampleSentence.setExampleSentenceLomaji(currentCell.getStringCellValue());
                    } else if (colNum == 7) {
                        currentExampleSentence.setExampleSentenceHoagi(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    colNum++;
                }

                exampleSentences.add(currentExampleSentence);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlExampleSentence currentExampleSentence : exampleSentences) {
                        realm.copyToRealmOrUpdate(currentExampleSentence);
                    }
                }
            });

            // add to TlDescription
            final RealmResults<TlTaigiWord> taigiWords = mRealm.where(TlTaigiWord.class).findAll();
            final SparseArray<TlTaigiWord> taigiWordSparseArray = new SparseArray<>();
            for (final TlTaigiWord taigiWord : taigiWords) {
                taigiWordSparseArray.put(taigiWord.getMainCode(), taigiWord);
            }
            final RealmResults<TlDescription> descriptions = mRealm.where(TlDescription.class).findAll();
            final SparseArray<TlDescription> descriptionSparseArray = new SparseArray<>();
            for (final TlDescription description : descriptions) {
                descriptionSparseArray.put(description.getMainCode(), description);
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (final TlExampleSentence currentExampleSentence : exampleSentences) {
                        final int mainCode = currentExampleSentence.getMainCode();
                        final TlTaigiWord foundTaigiWord = taigiWordSparseArray.get(mainCode);
                        if (foundTaigiWord == null) {
                            Log.e(TAG, "TlTaigiWord for TlExampleSentence's maincode = " + mainCode + " not found.");
                            continue;
                        }

                        final RealmList<TlDescription> foundTaigiWordDescriptions = foundTaigiWord.getDescriptions();
                        for (TlDescription description : foundTaigiWordDescriptions) {
                            if (description.getDescCode() == currentExampleSentence.getDescCode()) {
                                description.getExampleSentences().add(currentExampleSentence);
                            }
                        }
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseExampleSentence()");
    }

    private void parseThesaurus() {
        Log.d(TAG, "start: parseThesaurus()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_THESAURUS);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlThesaurus> thesauruses = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlThesaurus currentThesaurus = new TlThesaurus();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentThesaurus.setThesaurusCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentThesaurus.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentThesaurus.setThesaurusMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 4) {
                        currentThesaurus.setLomaji(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    colNum++;
                }

                thesauruses.add(currentThesaurus);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlThesaurus currentThesaurus : thesauruses) {
                        realm.copyToRealmOrUpdate(currentThesaurus);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseThesaurus()");
    }

    private void parseAnyonym() {
        Log.d(TAG, "start: parseAnyonym()");

        final HSSFWorkbook workbook = ExcelUtils.readExcelWorkbookFromAssetsFile(this, ASSETS_PATH_TAILO_DICT_ANTONYM);
        if (workbook != null) {
            final HSSFSheet firstSheet = workbook.getSheetAt(0);

            Iterator rowIterator = firstSheet.rowIterator();
            final ArrayList<TlAntonym> antonyms = new ArrayList<>();

            int rowNum = 1;
            while (rowIterator.hasNext()) {
                HSSFRow currentRow = (HSSFRow) rowIterator.next();
                if (rowNum == 1) {
                    rowNum++;
                    continue;
                }

                Iterator cellIterator = currentRow.cellIterator();

                final TlAntonym currentAntonym = new TlAntonym();

                int colNum = 1;
                while (cellIterator.hasNext()) {
                    HSSFCell currentCell = (HSSFCell) cellIterator.next();

                    if (colNum == 1) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAntonym.setAntonymCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 2) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAntonym.setMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 3) {
                        final String stringCellValue = currentCell.getStringCellValue();
                        currentAntonym.setAntonymMainCode(Integer.valueOf(stringCellValue));
                    } else if (colNum == 4) {
                        currentAntonym.setLomaji(currentCell.getStringCellValue());
                    } else {
                        break;
                    }

                    colNum++;
                }

                antonyms.add(currentAntonym);

                rowNum++;
            }

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (TlAntonym currentAntonym : antonyms) {
                        realm.copyToRealmOrUpdate(currentAntonym);
                    }
                }
            });
        }

        Log.d(TAG, "finish: parseAnyonym()");
    }
}
