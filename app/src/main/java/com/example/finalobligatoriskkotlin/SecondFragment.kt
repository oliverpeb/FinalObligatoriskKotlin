package com.example.finalobligatoriskkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalobligatoriskkotlin.databinding.FragmentSecondBinding
import com.example.finalobligatoriskkotlin.models.PersonsViewModel
import com.example.finalobligatoriskkotlin.models.MyAdapter
import com.google.firebase.auth.FirebaseAuth

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PersonsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userUID = FirebaseAuth.getInstance().currentUser?.uid
        if (userUID != null) {
            viewModel.getPersonsForUser(userUID)
        }

        // Set up the layout manager for the RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        // Set up the spinner (dropdown menu)
        val actionsAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.actionsArray,
            android.R.layout.simple_spinner_item
        )
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.actionsSpinner.adapter = actionsAdapter

        var selectedAction = ""
        binding.actionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedAction = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Nothing selected
            }
        }

        binding.applyActionButton.setOnClickListener {
            when (selectedAction) {
                "Sort by Name" -> viewModel.sortByName()
                "Sort by Name Descending" -> viewModel.sortByNameDescending()
                "Sort by Age" -> viewModel.sortByAge()
                "Sort by Age Descending" -> viewModel.sortByAgeDescending()
                "sort by Birthday" -> viewModel.sortByBirthday()
                "sort by Birthday Descending" -> viewModel.sortByBirthdayDescending()
            }
        }

        // Observe the personsLiveData
        viewModel.personsLiveData.observe(viewLifecycleOwner) { persons ->
            val adapter = MyAdapter(persons) { position ->
                val selectedPerson = viewModel.personsLiveData.value?.get(position)
                if (selectedPerson != null) {
                    val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(selectedPerson.id)
                    findNavController().navigate(action)
                }
            }
            binding.recyclerView.adapter = adapter
        }

        binding.buttonAdd.setOnClickListener{
            val action = SecondFragmentDirections.actionSecondFragmentToAddFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
