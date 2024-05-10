package si.uni_lj.fe.tnuv.cppaplikacija;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PreferencesManager {
    private static final String PREFERENCES_NAME = "quiz_preferences";
    private static final String ANSWERED_QUESTIONS_KEY = "answered_questions";
    private static final String FAVORITE_QUESTIONS_KEY = "favorite_questions";
    private static final String CORRECTLY_ANSWERED_QUESTIONS_KEY = "correctly_answered_questions";
    private static final String FALSELY_ANSWERED_QUESTIONS_KEY = "falsely_answered_questions";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    DatabaseManager databaseManager = new DatabaseManager();

    public PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // dodaj odgovorjeno vprašanje
    public void addAnsweredQuestion(int questionId, boolean isCorrect) {
        JsonObject jsonObject = getPreferencesAsJsonObject();

        // Add the question to the "answered_questions" array
        JsonArray answeredQuestionsArray = jsonObject.getAsJsonArray(ANSWERED_QUESTIONS_KEY);
        if (answeredQuestionsArray == null) {
            answeredQuestionsArray = new JsonArray();
        }
        // preverimo, če je že odgovoril na to vprašanje
        boolean containsQuestionId = false;
        for (JsonElement element : answeredQuestionsArray) {
            if (element.getAsInt() == questionId) {
                containsQuestionId = true;
                break;
            }
        }
        // shranimo, če ni
        if (!containsQuestionId) {
            answeredQuestionsArray.add(questionId);
            jsonObject.add(ANSWERED_QUESTIONS_KEY, answeredQuestionsArray);
        }

        // napačno/pravilno odgovorjena vprašanja
        JsonArray correctlyAnsweredQuestionsArray = jsonObject.getAsJsonArray(CORRECTLY_ANSWERED_QUESTIONS_KEY);
        JsonArray falselyAnsweredQuestionsArray = jsonObject.getAsJsonArray(FALSELY_ANSWERED_QUESTIONS_KEY);
        // če je pravilen odg
        if (isCorrect) {
            if (correctlyAnsweredQuestionsArray == null) {
                correctlyAnsweredQuestionsArray = new JsonArray();
            }
            // ali je že shranjeno?
            containsQuestionId = false;
            for (JsonElement element : correctlyAnsweredQuestionsArray) {
                if (element.getAsInt() == questionId) {
                    containsQuestionId = true;
                    break;
                }
            }
            // shrani če še ni
            if (!containsQuestionId) {
                correctlyAnsweredQuestionsArray.add(questionId);
                jsonObject.add(CORRECTLY_ANSWERED_QUESTIONS_KEY, correctlyAnsweredQuestionsArray);
            }
            // odstrani iz napačno odgovorjenih, če je tam
            if (falselyAnsweredQuestionsArray != null) {
                int index = -1;
                for (int i = 0; i < falselyAnsweredQuestionsArray.size(); i++) {
                    if (falselyAnsweredQuestionsArray.get(i).getAsInt() == questionId) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    falselyAnsweredQuestionsArray.remove(index);
                    jsonObject.add(FALSELY_ANSWERED_QUESTIONS_KEY, falselyAnsweredQuestionsArray);
                }
            }
        // če je odgovor napačen (ista zgodba kot pri if)
        } else {
            if (falselyAnsweredQuestionsArray == null) {
                falselyAnsweredQuestionsArray = new JsonArray();
            }
            containsQuestionId = false;
            for (JsonElement element : falselyAnsweredQuestionsArray) {
                if (element.getAsInt() == questionId) {
                    containsQuestionId = true;
                    break;
                }
            }
            if (!containsQuestionId) {
                falselyAnsweredQuestionsArray.add(questionId);
                jsonObject.add(FALSELY_ANSWERED_QUESTIONS_KEY, falselyAnsweredQuestionsArray);
            }
            if (correctlyAnsweredQuestionsArray != null) {
                int index = -1;
                for (int i = 0; i < correctlyAnsweredQuestionsArray.size(); i++) {
                    if (correctlyAnsweredQuestionsArray.get(i).getAsInt() == questionId) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    correctlyAnsweredQuestionsArray.remove(index);
                    jsonObject.add(CORRECTLY_ANSWERED_QUESTIONS_KEY, correctlyAnsweredQuestionsArray);
                }
            }
        }
        // sharnimo podatke
        savePreferencesFromJsonObject(jsonObject);
    }

    // dodajenje vprašanja pod priljubljena
    public boolean addRemoveFavoriteQuestion(int questionId) {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        // če še ni, ustvari tabelo
        JsonArray favoriteQuestionsArray = jsonObject.getAsJsonArray(FAVORITE_QUESTIONS_KEY);
        if (favoriteQuestionsArray == null) {
            favoriteQuestionsArray = new JsonArray();
            return false;
        }

        // ali že vsebuje
        int ixToRemove = -1;
        boolean containsQuestionId = false;
        for (int i = 0; i < favoriteQuestionsArray.size(); i++) {
            if (favoriteQuestionsArray.get(i).getAsInt() == questionId) {
                containsQuestionId = true;
                ixToRemove = i;
                break;
            }
        }
        // dodaj, če še ni
        if (!containsQuestionId) {
            favoriteQuestionsArray.add(questionId);
            jsonObject.add(FAVORITE_QUESTIONS_KEY, favoriteQuestionsArray);
            savePreferencesFromJsonObject(jsonObject);
            return true;
        } else { // izbriši če je že
            favoriteQuestionsArray.remove(ixToRemove);
            jsonObject.remove(FAVORITE_QUESTIONS_KEY);
            jsonObject.add(FAVORITE_QUESTIONS_KEY, favoriteQuestionsArray);
            savePreferencesFromJsonObject(jsonObject);
            return false;
        }
    }



    private JsonObject getPreferencesAsJsonObject() {
        String preferencesJson = sharedPreferences.getString(PREFERENCES_NAME, "{}");
        return gson.fromJson(preferencesJson, JsonObject.class);
    }

    private void savePreferencesFromJsonObject(JsonObject jsonObject) {
        String preferencesJson = gson.toJson(jsonObject);
        sharedPreferences.edit().putString(PREFERENCES_NAME, preferencesJson).apply();
    }

    // vrne vsa odgovorjena vprašanja
    public List<Integer> getAnsweredQuestions() {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        JsonArray answeredQuestionsArray = jsonObject.getAsJsonArray(ANSWERED_QUESTIONS_KEY);
        if (answeredQuestionsArray == null) {
            return new ArrayList<>();
        }
        List<Integer> answeredQuestions = new ArrayList<>();
        for (JsonElement element : answeredQuestionsArray) {
            answeredQuestions.add(element.getAsInt());
        }
        return answeredQuestions;
    }

    // vrne priljubjena vprašanja
    public ArrayList<Question> getFavoriteQuestions() {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        JsonArray favoriteQuestionsArray = jsonObject.getAsJsonArray(FAVORITE_QUESTIONS_KEY);
        if (favoriteQuestionsArray == null) {
            return new ArrayList<>();
        }
        ArrayList<Integer> favoriteQuestionsIds = new ArrayList<>();
        ArrayList<Question> favoriteQuestions = new ArrayList<Question>();
        for (JsonElement element : favoriteQuestionsArray) {
            favoriteQuestionsIds.add(element.getAsInt());
        }
        favoriteQuestions = databaseManager.getQuestionsByIds(favoriteQuestionsIds);
        return favoriteQuestions;
    }
    // vrne pravilno odg vprašanja
    public List<Integer> getCorrectlyAnsweredQuestions() {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        JsonArray correctlyAnsweredQuestionsArray = jsonObject.getAsJsonArray(CORRECTLY_ANSWERED_QUESTIONS_KEY);
        if (correctlyAnsweredQuestionsArray == null) {
            return new ArrayList<>();
        }
        List<Integer> correctlyAnsweredQuestions = new ArrayList<>();
        for (JsonElement element : correctlyAnsweredQuestionsArray) {
            correctlyAnsweredQuestions.add(element.getAsInt());
        }
        return correctlyAnsweredQuestions;
    }
    // vrne napačno odg vprašanja

    public List<Integer> getFalselyAnsweredQuestions() {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        JsonArray falselyAnsweredQuestionsArray = jsonObject.getAsJsonArray(FALSELY_ANSWERED_QUESTIONS_KEY);
        if (falselyAnsweredQuestionsArray == null) {
            return new ArrayList<>();
        }
        List<Integer> falselyAnsweredQuestions = new ArrayList<>();
        for (JsonElement element : falselyAnsweredQuestionsArray) {
            falselyAnsweredQuestions.add(element.getAsInt());
        }
        return falselyAnsweredQuestions;
    }

    public int getTotalAnsweredQuestions() {
        return getAnsweredQuestions().size();
    }

    public int getTotalCorrectlyAnsweredQuestions() {
        return getCorrectlyAnsweredQuestions().size();
    }

    public int getTotalFalselyAnsweredQuestions() {
        return getFalselyAnsweredQuestions().size();
    }


    public boolean isQuestionFavorite(int questionId) {
        JsonObject jsonObject = getPreferencesAsJsonObject();
        JsonArray favoriteQuestionsArray = jsonObject.getAsJsonArray(FAVORITE_QUESTIONS_KEY);
        if (favoriteQuestionsArray != null) {
            for (JsonElement element : favoriteQuestionsArray) {
                if (element.getAsInt() == questionId) {
                    return true;
                }
            }
        }
        return false;
    }
}
