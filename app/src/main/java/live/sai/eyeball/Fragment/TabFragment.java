package live.sai.eyeball.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import live.sai.eyeball.R;

public class TabFragment extends Fragment {
    // Initialize variable
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        // Assign variable
        textView = view.findViewById(R.id.text_view);

        // Get Title
        String title = getArguments().getString("title");

        // Set title on text view
        textView.setText(title);

        // return view
        return view;
    }
}
