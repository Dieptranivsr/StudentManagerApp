package android.bignerdranch.studentmanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("Creator: Nguyen Duy Tu, Vu Minh Tuan, Dao Duc Dung, Nguyen Van Dat, Tran Tuan Diep.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}