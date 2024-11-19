package ro.upt.sma.heart

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ro.upt.sma.heart.model.HeartMeasurement
import ro.upt.sma.heart.presenters.assistant.HeartAssistantPresenter
import ro.upt.sma.heart.presenters.assistant.HeartAssistantView
import ro.upt.upt.sma.heart.injection.Injection
import java.text.MessageFormat

class HeartAssistantActivity : AppCompatActivity(), HeartAssistantView {

    private lateinit var presenter: HeartAssistantPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_heart_assistant)

        findViewById<RecyclerView>(R.id.rv_heart_assistant_measurement_list).layoutManager = LinearLayoutManager(this)

        // FIXME: Add the specific WEAR device code (as listed under 'Monitoring code' label)
        val code = "e325"
        this.presenter = Injection.provideHeartAssistantPresenter(code)
    }

    override fun onResume() {
        super.onResume()

        presenter.bind(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unbind()
    }

    override fun showLast(heartMeasurement: HeartMeasurement) {
        Log.d(TAG, "showLast: $heartMeasurement")

        findViewById<TextView>(R.id.tv_heart_assistant_instant_value).text =
            MessageFormat.format("{0} bpm", heartMeasurement.value)
        findViewById<TextView>(R.id.tv_heart_assistant_instant_date).text =
            DateUtils.getRelativeTimeSpanString(
                heartMeasurement.timestamp,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )
    }

    override fun showList(heartMeasurementList: List<HeartMeasurement>) {
        Log.d(TAG, "showList: " + heartMeasurementList.size)

        findViewById<RecyclerView>(R.id.rv_heart_assistant_measurement_list)?.adapter = MeasurementsAdapter(heartMeasurementList)
    }

    companion object {
        private val TAG = HeartAssistantActivity::class.java.simpleName
    }

}
