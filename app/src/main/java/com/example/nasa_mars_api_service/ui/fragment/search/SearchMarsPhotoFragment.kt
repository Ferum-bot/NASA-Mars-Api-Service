package com.example.nasa_mars_api_service.ui.fragment.search

import android.animation.LayoutTransition
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.enums.MarsDateTypes
import com.example.nasa_mars_api_service.core.enums.MarsRovers
import com.example.nasa_mars_api_service.core.enums.MarsRoversCamera
import com.example.nasa_mars_api_service.core.getSearchDateFormatFrom
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
                    (it as Button).text = viewModel.date
                    chooseDateButtonState = ButtonStates.HIDDEN
                }
                ButtonStates.HIDDEN -> {
                    showDateChooseViews()
                    (it as Button).text = getString(R.string.choose_date)
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
                    (it as Button).text = viewModel.rover!!.name
                    chooseRoverButtonState = ButtonStates.HIDDEN
                }
                ButtonStates.HIDDEN -> {
                    showRoverChooseViews()
                    (it as Button).text = getString(R.string.choose_rover)
                    chooseRoverButtonState = ButtonStates.INFORMATION_CHOSEN
                }
            }
        }

        binding.chooseCameraButton.setOnClickListener {
            if (roverIsNotChosen()) {
                showErrorMessage(R.string.choose_rover_firstly)
                return@setOnClickListener
            }
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
                    (it as Button).text = getString(R.string.choose_camera)
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
            }
        }

        binding.searchButton.setOnClickListener {
            if (viewModel.searchParametersAreValid()) {
                val params = viewModel.getSearchParameters()
                findNavController().navigate(SearchMarsPhotoFragmentDirections.actionSearchMarsPhotoFragmentToSearchListFragment(params))
            }
            else {
                getInvalidParamsAndShowErrorMessage()
            }
        }

        binding.selectDateButton.setOnClickListener {
            callDatePickerFragment()
        }


        binding.chooseRoverRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            chooseRoverButtonState = ButtonStates.INFORMATION_CHOSEN
            chooseCameraButtonState = ButtonStates.INFORMATION_NOT_CHOSEN
            hideRoverCameraChooseViews()
            binding.chooseCameraRadioGroup.clearCheck()
            when(checkedId) {
                binding.curiosityRadioButton.id -> {
                    viewModel.rover = MarsRovers.CURIOSITY
                }
                binding.opportunityRadioButton.id -> {
                    viewModel.rover = MarsRovers.OPPORTUNITY
                }
                binding.spiritRadioButton.id -> {
                    viewModel.rover = MarsRovers.SPIRIT
                }
            }
        }


        binding.chooseCameraRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                binding.CHEMCAMRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.CHEMCAM
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.FHAZRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.FHAZ
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.MAHLIRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.MAHLI
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.MARDIRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.MARDI
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.MASTRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.MAST
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.MINITESRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.MINITES
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.NAVCAMRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.NAVCAM
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.PANCAMRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.PANCAM
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                binding.RHAZRadioButton.id -> {
                    viewModel.camera = MarsRoversCamera.RHAZ
                    chooseCameraButtonState = ButtonStates.INFORMATION_CHOSEN
                }
            }
        }

        binding.switchEathDate.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    viewModel.dateType = MarsDateTypes.EARTH_DATE
                    binding.dateEditText.visibility = View.GONE
                    binding.selectDateButton.visibility = View.VISIBLE
                    if (binding.switchSol.isChecked) {
                        binding.switchSol.isChecked = false
                    }
                }
                false -> {
                    if (!binding.switchSol.isChecked) {
                        binding.dateEditText.visibility = View.GONE
                        binding.selectDateButton.visibility = View.GONE
                    }
                }
            }
        }

        binding.switchSol.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    viewModel.dateType = MarsDateTypes.MARS_SOL
                    binding.dateEditText.visibility = View.VISIBLE
                    binding.selectDateButton.visibility = View.GONE
                    if (binding.switchEathDate.isChecked) {
                        binding.switchEathDate.isChecked = false
                    }
                }
                false -> {
                    if (!binding.switchEathDate.isChecked) {
                        binding.dateEditText.visibility = View.GONE
                        binding.selectDateButton.visibility = View.GONE
                    }
                }
            }
        }

        binding.appBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.dateEditText.setOnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val editText = view as EditText
                val date = editText.text.toString()
                if (viewModel.isDateCorrect(date)) {
                    viewModel.setNewDate(date)
                    chooseDateButtonState = ButtonStates.INFORMATION_CHOSEN
                }
                else {
                    if (binding.switchEathDate.isChecked) {
                        showErrorMessage(R.string.incorrect_date_earth_date)
                    }
                    else {
                        showErrorMessage(R.string.incorrect_date_mars_sol)
                    }
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun roverIsNotChosen(): Boolean {
        return !(chooseRoverButtonState == ButtonStates.HIDDEN || chooseRoverButtonState == ButtonStates.INFORMATION_CHOSEN)
    }

    private fun showErrorMessage(message: String) {
        val context = requireContext()
        Toast.makeText(context, R.string.choose_rover_firstly, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(messageId: Int) {
        val context = requireContext()
        Toast.makeText(context, getString(messageId), Toast.LENGTH_SHORT).show()
    }

    private fun getInvalidParamsAndShowErrorMessage() {
        if (viewModel.dateType == null) {
            showErrorMessage(R.string.choose_date_firstly)
            return
        }
        if (viewModel.date == null) {
            showErrorMessage(R.string.choose_date_firstly)
            return
        }
        if (viewModel.rover == null) {
            showErrorMessage(R.string.choose_rover_firstly)
            return
        }
        if (viewModel.camera == null) {
            showErrorMessage(R.string.choose_camera_firstly)
            return
        }
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

    private fun callDatePickerFragment() {
        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH)
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val context = requireContext()
        val datePickerDialog = DatePickerDialog(context, { view, year, month, dayOfMonth ->
            val date = getSearchDateFormatFrom(dayOfMonth, month, year)
            binding.selectDateButton.text = date
            chooseDateButtonState = ButtonStates.INFORMATION_CHOSEN
            viewModel.setNewDate(date)
        },
        year, month, day )
        datePickerDialog.show()
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
        binding.selectDateButton.visibility = View.GONE
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