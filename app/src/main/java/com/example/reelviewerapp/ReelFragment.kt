import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.reelviewerapp.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.snackbar.Snackbar


class ReelFragment : Fragment() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private var videoUrl: String? = null
    private lateinit var videoTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoUrl = arguments?.getString("VIDEO_URL")
        videoTitle = arguments?.getString("VIDEO_TITLE") ?: "Default Title"
        Log.d("ReelFragment", "onCreate - Video URL: $videoUrl")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        val titleView: TextView = view.findViewById(R.id.video_title)
        titleView.text = videoTitle
        titleView.visibility = View.VISIBLE
        initializePlayer()
        Log.d("ReelFragment", "onViewCreated - Player initialized")
    }

    private fun initializePlayer() {
        if (!isNetworkAvailable()) {
            showError("No internet connection. Please check your network settings.")
            return
        }

        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player
        val videoUri = Uri.parse(videoUrl ?: "https://path/to/default/video.mp4")
        val mediaItem = MediaItem.fromUri(videoUri)
        player.setMediaItem(mediaItem)

        player.prepare()
        player.playWhenReady = true

        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                showError("Playback error: ${error.message}")
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showError(message: String) {
        Snackbar.make(playerView, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
        Log.d("ReelFragment", "onStart - Playback started")
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
        Log.d("ReelFragment", "onResume - Playback resumed")
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        Log.d("ReelFragment", "onPause - Playback paused")
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
        Log.d("ReelFragment", "onStop - Playback stopped")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerView.player = null
        Log.d("ReelFragment", "onDestroyView - Player view detached")
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        Log.d("ReelFragment", "onDestroy - Player released")
    }

    companion object {
        fun newInstance(videoUrl: String, videoTitle: String): ReelFragment {
            val fragment = ReelFragment()
            val args = Bundle().apply {
                putString("VIDEO_URL", videoUrl)
                putString("VIDEO_TITLE", videoTitle)
            }
            fragment.arguments = args
            return fragment
        }
    }

}
