package com.example.finalobligatoriskkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.finalobligatoriskkotlin.models.Person
import com.example.finalobligatoriskkotlin.models.PersonsViewModel
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.finalobligatoriskkotlin.databinding.FragmentThirdBinding

class ThirdFragment : Fragment() {

    private val viewModel: PersonsViewModel by activityViewModels()
    private var selectedPerson: Person? = null
    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ThirdFragment", "onCreate called")

        // Using Safe Args to retrieve the passed personId argument
        val args = ThirdFragmentArgs.fromBundle(requireArguments())
        val personId = args.personId
        Log.d("ThirdFragment", "Person ID from args: $personId")
        selectedPerson = viewModel.getPersonById(personId)
        Log.d("ThirdFragment", "Selected person from ViewModel: ${selectedPerson?.name}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ThirdFragment", "onViewCreated called")

        val nameTextView = binding.tvPersonName
        val birthYearTextView = binding.tvPersonBirthYear
        val birthMonthTextView = binding.tvPersonBirthMonth
        val birthDayTextView = binding.tvPersonBirthDay
        val remarksTextView = binding.tvPersonRemarks
        val personImageView = binding.ivPersonPicture
        val deleteButton = binding.buttonDelete
        val updateButton = binding.buttonUpdate
        val ageTextView = binding.tvPersonAge
        val birthdayTextView = binding.tvPersonBirthday


        nameTextView.text = selectedPerson?.name
        Log.d("ThirdFragment", "Setting nameTextView to: ${selectedPerson?.name}")
        birthYearTextView.text = "Birth Year: ${selectedPerson?.birthYear}"
        birthMonthTextView.text = "Birth Month: ${selectedPerson?.birthMonth}"
        birthDayTextView.text = "Birth Day Of Month: ${selectedPerson?.birthDayOfMonth}"
        remarksTextView.text = "Remarks: ${selectedPerson?.remarks}"
        ageTextView.text = "Age: ${selectedPerson?.age}"
        birthdayTextView.text = "Birthday: ${selectedPerson?.birthDayOfMonth}/${selectedPerson?.birthMonth}/${selectedPerson?.birthYear}"

        binding.buttonDelete.setOnClickListener {
            selectedPerson?.let {
                Log.d("Pear", "Delete button clicked")
                viewModel.delete(it.id)
                Log.d("Pear", "Person deleted with ID: ${it.id}")
                findNavController().popBackStack()
            }
        }

        binding.buttonUpdate.setOnClickListener{
            val name = binding.editTextName.text.toString()
            val birthYear = binding.editTextBirthYear.text.toString().toIntOrNull()
            val birthMonth = binding.editTextBirthMonth.text.toString().toIntOrNull()
            val birthDayOfMonth = binding.editTextBirthDayOfMonth.text.toString().toIntOrNull()
            val remarks = selectedPerson?.remarks ?: ""
            val pictureUrl = selectedPerson?.pictureUrl

            if (birthYear != null && birthMonth != null && birthDayOfMonth != null) {
                val updatedPerson = selectedPerson?.copy(
                    name = name,
                    birthYear = birthYear,
                    birthMonth = birthMonth,
                    birthDayOfMonth = birthDayOfMonth,
                    remarks = if(remarks.isBlank()) null else remarks,
                    pictureUrl = pictureUrl
                )


                if (updatedPerson != null) {
                    viewModel.update(updatedPerson)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "An error occurred while updating.", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(context, "Please ensure all fields are filled out correctly.", Toast.LENGTH_SHORT).show()
            }

        }


    }


    companion object {
        @JvmStatic
        fun newInstance(personId: Int) = ThirdFragment().apply {
            arguments = Bundle().apply {
                putInt("personId", personId) // Using "personId" string key directly as it's generated by Safe Args
            }
        }
    }
}
