package com.example.finalobligatoriskkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.finalobligatoriskkotlin.models.Person
import com.example.finalobligatoriskkotlin.models.PersonsViewModel
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.finalobligatoriskkotlin.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth

class AddFragment : Fragment() {

    private val viewModel: PersonsViewModel by activityViewModels()
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AddFragment", "onViewCreated called")

        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val birthYear = binding.editTextBirthYear.text.toString().toIntOrNull()
            val birthMonth = binding.editTextBirthMonth.text.toString().toIntOrNull()
            val birthDayOfMonth = binding.editTextBirthDayOfMonth.text.toString().toIntOrNull()
            val remarks = binding.editTextRemarks.text.toString()
            val pictureUrl = binding.editTextPictureUrl.text.toString()
            val userUID = FirebaseAuth.getInstance().currentUser?.uid

            if (name.isNotBlank() && birthYear != null && birthMonth != null && birthDayOfMonth != null &&
                isValidDate(birthYear, birthMonth, birthDayOfMonth) && userUID != null
            ) {
                val newPerson = Person(
                    id = 0,
                    userId = userUID,
                    name = name,
                    birthYear = birthYear,
                    birthMonth = birthMonth,
                    birthDayOfMonth = birthDayOfMonth,
                    remarks = if (remarks.isBlank()) null else remarks,
                    pictureUrl = if (pictureUrl.isBlank()) null else pictureUrl
                )

                viewModel.add(newPerson, userUID)
                findNavController().popBackStack()
            } else {
                var errorMessage = "Please ensure all required fields are filled out correctly. "

                if (birthMonth != null && (birthMonth < 1 || birthMonth > 12)) {
                    errorMessage += "Invalid month. "
                }

                if (birthDayOfMonth != null && (birthDayOfMonth < 1 || birthDayOfMonth > 31)) {
                    errorMessage += "Invalid day. "
                }

                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Helper function to check if the date is valid
    private fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> day in 1..31
            4, 6, 9, 11 -> day in 1..30
            2 -> if (isLeapYear(year)) day in 1..29 else day in 1..28
            else -> false
        }
    }

    // Helper function to check for leap years
    private fun isLeapYear(year: Int): Boolean {
        return when {
            year % 400 == 0 -> true
            year % 100 == 0 -> false
            year % 4 == 0 -> true
            else -> false
        }
    }
}
