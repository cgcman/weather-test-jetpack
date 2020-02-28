package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.test.R
import com.example.test.viewmodels.WelcomeViewModel
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.viewmodel.ext.android.viewModel


class WelcomeFrag : BaseFrag() {

    val welcomeViewModel: WelcomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getActionBar()?.setTitle(getResources().getString(R.string.hello));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonWelcome.setOnClickListener {
            val action = WelcomeFragDirections.actionWelcomeFragToWeatherFrag()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        welcomeViewModel.checkUserExist()
        if(welcomeViewModel.checkUserExist().equals("")){
            buttonWelcome.visibility = View.GONE
        } else{
            nameInput.setText(welcomeViewModel.checkUserExist())
            buttonWelcome.visibility = View.VISIBLE
        }

        nameInput.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                saveInputName(s.toString())
            }
        })

        observeViewModel()
    }

    fun observeViewModel(){

        welcomeViewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                if(it.equals("")){
                    buttonWelcome.visibility = View.GONE
                } else{
                    buttonWelcome.visibility = View.VISIBLE
                }
            }
        })
    }

    fun saveInputName(user:String){
        welcomeViewModel.saveUserName(user)
    }

}
