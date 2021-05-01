package co.ghostnotes.sample.recompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import co.ghostnotes.sample.recompose.ui.theme.RecomposeTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecomposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }

        observeViewModel()
        viewModel.countUp()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.count.collect {
                //Timber.d("### received: count=$it")
            }
        }
    }
}

@Composable
fun Greeting(name: String, viewModel: MainViewModel = viewModel()) {
    val count = viewModel.count.collectAsState(0)
    GreetingContent(name = name, count = count.value)
}

@Composable
fun GreetingContent(name: String, count: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello $name! $count",
            style = MaterialTheme.typography.h6,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecomposeTestTheme {
        GreetingContent("Android", 999)
    }
}