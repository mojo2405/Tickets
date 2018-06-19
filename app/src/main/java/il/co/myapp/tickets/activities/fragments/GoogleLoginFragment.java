package il.co.myapp.tickets.activities.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import il.co.myapp.tickets.activities.LoginActivity;
import il.co.myapp.tickets.R;
import il.co.myapp.tickets.model.User;

public class GoogleLoginFragment extends Fragment {

    private static final String TAG = GoogleLoginFragment.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;

    public User user = new User();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.google_login, container, false);
        view.findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        view.findViewById(R.id.disconnectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleSignIn();
    }

    public void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Check if signed in in Facebook
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isFacebookLoggedIn = accessToken != null && !accessToken.isExpired();

        if (!isFacebookLoggedIn && GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
            mGoogleSignInClient.silentSignIn().addOnCompleteListener(getActivity(), new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                    if (task.isSuccessful()) {
                        // The signed in account is stored in the task's result.
                        GoogleSignInAccount signedInAccount = task.getResult();
                        user.setAccessToken(signedInAccount.getIdToken());
                        Log.d(TAG, "Got new access token " + signedInAccount.getIdToken());
                    } else {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account == null) {
            view.findViewById(R.id.google_sign_in_button).setVisibility(View.VISIBLE);
            view.findViewById(R.id.disconnectButton).setVisibility(View.GONE);

        } else {
            view.findViewById(R.id.google_sign_in_button).setVisibility(View.GONE);
            view.findViewById(R.id.disconnectButton).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().recreate();
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // Signed in successfully, show authenticated UI.
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());
            user.setAccessToken(idToken);
            user.setLoginType(user.GOOGLE);

            ((LoginActivity) getActivity()).loginSuccess(user);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);


        }
    }
}

