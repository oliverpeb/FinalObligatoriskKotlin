package com.example.finalobligatoriskkotlin

// Importing required packages and classes
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
import android.view.GestureDetector  // Imported for swipe gesture detection
import android.view.MotionEvent  // Imported for swipe gesture detection

// AddFragment class definition
class AddFragment : Fragment() {

    // ViewModel and ViewBinding declarations
    private val viewModel: PersonsViewModel by activityViewModels()
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    // Gesture Detector for swipe gestures
    private lateinit var gestureDetector: GestureDetector

    // Companion object for constants
    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    // onCreateView method
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated method
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AddFragment", "onViewCreated called")

        // Initializing gesture detector
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                e1?.let { start ->
                    e2?.let { end ->
                        if (end.x - start.x > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            // Swipe right detected
                            onSwipeRight()
                            return true
                        }
                    }
                }
                return false
            }
        })

        // Setting onTouchListener for the root view
        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        // Set onClickListener for the save button
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val birthYear = binding.editTextBirthYear.text.toString().toIntOrNull()
            val birthMonth = binding.editTextBirthMonth.text.toString().toIntOrNull()
            val birthDayOfMonth = binding.editTextBirthDayOfMonth.text.toString().toIntOrNull()
            val remarks = binding.editTextRemarks.text.toString()
            val pictureUrl = binding.editTextPictureUrl.text.toString()
            val userUID = FirebaseAuth.getInstance().currentUser?.uid

            // Validation checks
            if (name.isNotBlank() && birthYear != null && birthMonth != null && birthDayOfMonth != null &&
                isValidDate(birthYear, birthMonth, birthDayOfMonth) && userUID != null
            ) {
                // Creating a new person object
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

                // Adding the person to the ViewModel
                viewModel.add(newPerson, userUID)

                // Returning to the previous fragment
                findNavController().popBackStack()
            } else {
                var errorMessage = "Please ensure all required fields are filled out correctly. "

                // Month validation
                if (birthMonth != null && (birthMonth < 1 || birthMonth > 12)) {
                    errorMessage += "Invalid month. "
                }

                // Day validation
                if (birthDayOfMonth != null && (birthDayOfMonth < 1 || birthDayOfMonth > 31)) {
                    errorMessage += "Invalid day. "
                }

                // Displaying error message
                Toast.makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // onDestroyView method
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

    // Function to handle swipe right gesture
    private fun onSwipeRight() {
        Toast.makeText(context, "Swipe Right Detected", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
        // Add additional logic for swipe right here if needed
    }
}
