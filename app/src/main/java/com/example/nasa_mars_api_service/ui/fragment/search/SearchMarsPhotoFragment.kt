package com.example.nasa_mars_api_service.ui.fragment.search

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.databinding.FragmentSearchBinding
import com.google.android.material.switchmaterial.SwitchMaterial


/**
 * Created by Matvey Popov.
 * Date: 31.12.2020
 * Time: 20:25
 * Project: NASA-Mars-API-Service
 */
class SearchMarsPhotoFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: SearchMarsPhotoViewModel

    private var chooseDateButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
    private var chooseRoverButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
    private var chooseCameraButtonState = ButtonStates.INFORMATION_NOT_CHOSEN

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val factory = SearchMarsPhotoViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(SearchMarsPhotoViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        setUpAnimations()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBaseVisibilityForViews()
        setAllClickListeners()
        setEditTextListener()
    }

    private fun setUpBaseVisibilityForViews() {
        hideDateChooseViews()
        hideRoverChooseViews()
        hideRoverCameraChooseViews()
    }

    private fun setAllClickListeners() {

        binding.chooseDateButton.setOnClickListener {
            when(chooseDateButtonState) {
                ButtonStates.INFORMATION_NOT_CHOSEN -> {
                    showDateChooseViews()
                    chooseDateButtonState = ButtonStates.INFORMATION_IS_CHOOSING
                }
                ButtonStates.INFORMATION_CHOSEN -> {
                    hideDateChooseViews()
                    (it as Button).text = viewModel.getChosenDate()
                    chooseDateButtonState = ButtonStates.HIDDEN
                }
                ButtonStates.HIDDEN -> {
                    showDateChooseViews()
                    binding.dateEditText.visibility = View.VISIBLE
                    chooseDateButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                ButtonStates.INFORMATION_IS_CHOOSING -> {
                    hideDateChooseViews()
                    (it as Button).text = getString(R.string.choose_date)
                    chooseDateButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
                }
            }
        }

        binding.chooseRoverButton.setOnClickListener {
            when(chooseRoverButtonState) {
                ButtonStates.INFORMATION_NOT_CHOSEN -> {
                    showRoverChooseViews()
                    chooseRoverButtonState = ButtonStates.INFORMATION_IS_CHOOSING
                }
                ButtonStates.INFORMATION_IS_CHOOSING -> {
                    hideRoverChooseViews()
                    (it as Button).text = getString(R.string.choose_rover)
                    chooseRoverButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
                }
                ButtonStates.INFORMATION_CHOSEN -> {
                    hideRoverChooseViews()
                    (it as Button).text = viewModel.getChosenRoverName()
                    chooseRoverButtonState = ButtonStates.HIDDEN
                }
                ButtonStates.HIDDEN -> {
                    showRoverChooseViews()
                    chooseRoverButtonState = ButtonStates.INFORMATION_CHOSEN
                }
            }
        }

        binding.chooseCameraButton.setOnClickListener {
//            if (roverIsNotChosen()) {
//                showErrorMessage()
//                return@setOnClickListener
//            }
            val cameras = viewModel.getAvailableRoverCameras()
            when(chooseCameraButtonState) {
                ButtonStates.INFORMATION_NOT_CHOSEN -> {
                    showRoverCamerasChooseViews(cameras)
                    chooseCameraButtonState = ButtonStates.INFORMATION_IS_CHOOSING
                }
                ButtonStates.INFORMATION_CHOSEN -> {
                    hideRoverCameraChooseViews()
                    (it as Button).text = viewModel.getChosenRoverCameraShortName()
                    chooseCameraButtonState = ButtonStates.HIDDEN
                }
                ButtonStates.INFORMATION_IS_CHOOSING -> {
                    hideRoverCameraChooseViews()
                    (it as Button).text = getString(R.string.choose_camera)
                    chooseCameraButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
                }
                ButtonStates.HIDDEN -> {
                    showRoverCamerasChooseViews(cameras)
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
            }
        }

        binding.searchButton.setOnClickListener {

        }

        binding.switchEathDate.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    binding.dateEditText.visibility = View.VISIBLE
                    if (binding.switchSol.isChecked) {
                        binding.switchSol.isChecked = false
                    }
                }
                false -> {
                    if (!binding.switchSol.isChecked) {
                        binding.dateEditText.visibility = View.GONE
                    }
                }
            }
        }

        binding.switchSol.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    binding.dateEditText.visibility = View.VISIBLE
                    if (binding.switchEathDate.isChecked) {
                        binding.switchEathDate.isChecked = false
                    }
                }
                false -> {
                    if (!binding.switchEathDate.isChecked) {
                        binding.dateEditText.visibility = View.GONE
                    }
                }
            }
        }

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun roverIsNotChosen(): Boolean {
        return !(chooseRoverButtonState == ButtonStates.HIDDEN || chooseRoverButtonState == ButtonStates.INFORMATION_CHOSEN)
    }

    private fun showErrorMessage() {
        val context = requireContext()
        Toast.makeText(context, R.string.choose_rover_firstly, Toast.LENGTH_SHORT).show()
    }

    private fun showDateChooseViews() {
        binding.dateTypeLabel.visibility = View.VISIBLE
        binding.switchSol.visibility = View.VISIBLE
        binding.switchEathDate.visibility = View.VISIBLE
        binding.marsSolLabel.visibility = View.VISIBLE
        binding.eathDateLabel.visibility = View.VISIBLE

        if (binding.switchEathDate.isChecked || binding.switchSol.isChecked) {
            binding.dateEditText.visibility = View.VISIBLE
        }
    }

    private fun showRoverChooseViews() {
        binding.chooseRoverRadioGroup.visibility = View.VISIBLE
        binding.spiritRadioButton.visibility = View.VISIBLE
        binding.opportunityRadioButton.visibility = View.VISIBLE
        binding.curiosityRadioButton.visibility = View.VISIBLE
    }

    private fun showRoverCamerasChooseViews(cameras: List<MarsRoversCamera>) {
        binding.chooseCameraRadioGroup.visibility = View.VISIBLE
        if (cameras.contains(MarsRoversCamera.MINITES)) {
            binding.MINITESRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.PANCAM)) {
            binding.PANCAMRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.NAVCAM)) {
            binding.NAVCAMRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.RHAZ)) {
            binding.RHAZRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.FHAZ)) {
            binding.FHAZRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.MARDI)) {
            binding.MARDIRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.MAHLI)) {
            binding.MAHLIRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.CHEMCAM)) {
            binding.CHEMCAMRadioButton.visibility = View.VISIBLE
        }
        if (cameras.contains(MarsRoversCamera.MAST)) {
            binding.MASTRadioButton.visibility = View.VISIBLE
        }
    }

    private fun hideRoverCameraChooseViews() {
        binding.chooseCameraRadioGroup.visibility = View.GONE
        binding.CHEMCAMRadioButton.visibility = View.GONE
        binding.FHAZRadioButton.visibility = View.GONE
        binding.MAHLIRadioButton.visibility = View.GONE
        binding.MARDIRadioButton.visibility = View.GONE
        binding.MASTRadioButton.visibility = View.GONE
        binding.MINITESRadioButton.visibility = View.GONE
        binding.NAVCAMRadioButton.visibility = View.GONE
        binding.PANCAMRadioButton.visibility = View.GONE
        binding.RHAZRadioButton.visibility = View.GONE
    }

    private fun hideDateChooseViews() {
        binding.dateTypeLabel.visibility = View.GONE
        binding.switchSol.visibility = View.GONE
        binding.switchEathDate.visibility = View.GONE
        binding.marsSolLabel.visibility = View.GONE
        binding.eathDateLabel.visibility = View.GONE
        binding.dateEditText.visibility = View.GONE
    }

    private fun hideRoverChooseViews() {
        binding.chooseRoverRadioGroup.visibility = View.GONE
        binding.curiosityRadioButton.visibility = View.GONE
        binding.opportunityRadioButton.visibility = View.GONE
        binding.spiritRadioButton.visibility = View.GONE
    }

    private fun setEditTextListener() {

    }

    private fun setUpAnimations() {
        var layoutTransition = binding.mainViewGroup.layoutTransition
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        layoutTransition = binding.chooseDateLayout.layoutTransition
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private enum class ButtonStates{
        INFORMATION_NOT_CHOSEN, INFORMATION_CHOSEN, HIDDEN, INFORMATION_IS_CHOOSING
    }

}