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

        binding.buttonSave.setOnClickListener{
            val name = binding.editTextName.text.toString()
            val birthYear = binding.editTextBirthYear.text.toString().toIntOrNull()
            val birthMonth = binding.editTextBirthMonth.text.toString().toIntOrNull()
            val birthDayOfMonth = binding.editTextBirthDayOfMonth.text.toString().toIntOrNull()
            val remarks = binding.editTextRemarks.text.toString()
            val pictureUrl = binding.editTextPictureUrl.text.toString()

            if (name.isNotBlank() && birthYear != null && birthMonth != null && birthMonth in 1..12 && birthDayOfMonth != null && birthDayOfMonth in 1..31) {
                val newPerson = Person(
                    id = 0,
                    userId = "",
                    name = name,
                    birthYear = birthYear,
                    birthMonth = birthMonth,
                    birthDayOfMonth = birthDayOfMonth,
                    remarks = if (remarks.isBlank()) null else remarks,
                    pictureUrl = if (pictureUrl.isBlank()) null else pictureUrl
                )

                viewModel.add(newPerson)
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Please ensure all required fields are filled out correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
