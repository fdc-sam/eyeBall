package live.sai.eyeball.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import live.sai.eyeball.Inum.Gender;

public class User implements Parcelable {
    private String username;
    private String password;
    private String selectedRole;
    private String selectedDay;
    private String selectedMonth;
    private String selectedYear;
    private Gender selectedGender;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userId; // Add user ID field
    private String userBirthDate;

    public User() {
    }

    public User(String userId, String username, String password, String selectedRole, String selectedDay, String selectedMonth, String selectedYear, Gender gender, String email, String firstName, String middleName, String lastName, String userBirthDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.selectedRole = selectedRole;
        this.selectedDay = selectedDay;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        this.selectedGender = gender;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.userBirthDate = userBirthDate;
    }

    public User(String userId, String username, String password, String selectedRole, String selectedDay, String selectedMonth, String selectedYear, Gender gender, String email, String firstName, String middleName, String lastName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.selectedRole = selectedRole;
        this.selectedDay = selectedDay;
        this.selectedMonth = selectedMonth;
        this.selectedYear = selectedYear;
        this.selectedGender = gender;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    protected User(Parcel in) {
        username = in.readString();
        password = in.readString();
        selectedRole = in.readString();
        selectedDay = in.readString();
        selectedMonth = in.readString();
        selectedYear = in.readString();
        selectedGender = Gender.valueOf(in.readString());
        email = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        userId = in.readString();
        userBirthDate = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(selectedRole);
        dest.writeString(selectedDay);
        dest.writeString(selectedMonth);
        dest.writeString(selectedYear);
        dest.writeString(selectedGender.name());
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(userId);
        dest.writeString(userBirthDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public String getSelectedDay() {
        return selectedDay;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public Gender getGender() {
        return selectedGender;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateCreate() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(userBirthDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userBirthDate;
    }
}
