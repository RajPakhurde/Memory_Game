package com.rajpakhurde.memorygame

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rajpakhurde.memorygame.models.BoardSize
import com.rajpakhurde.memorygame.models.MemoryCard
import com.rajpakhurde.memorygame.models.MemoryGame
import com.rajpakhurde.memorygame.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {



    private lateinit var clRoot: ConstraintLayout
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter
    private val boardSize: BoardSize = BoardSize.EASY

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        tvNumPairs.setTextColor(ContextCompat.getColor(this,R.color.color_progress_none))
        memoryGame = MemoryGame(boardSize)
        adapter = MemoryBoardAdapter(this,boardSize,memoryGame.cards, object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }

        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())

    }

    private fun updateGameWithFlip(position: Int) {
        //Error checking
        if(memoryGame.haveWonGame()){
            // Alert the user of an invalid move
            Snackbar.make(clRoot, "You already won!",Snackbar.ANIMATION_MODE_SLIDE).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            // Alert the use of an invalid move
            Snackbar.make(clRoot, "Invalid move!",Snackbar.ANIMATION_MODE_SLIDE).show()
            return
        }
         if(memoryGame.flipCard(position)){
             Log.i(TAG,"Found a match! Num Paris Found : ${memoryGame.numPairsFound}")
             val color = ArgbEvaluator().evaluate(
                 memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                 ContextCompat.getColor(this,R.color.color_progress_none),
                 ContextCompat.getColor(this,R.color.color_progress_full)
             ) as Int
             tvNumPairs.setTextColor(color)
             tvNumPairs.text = "Paris: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
             if(memoryGame.haveWonGame()){
                 Snackbar.make(clRoot, "You won! Congratulations",Snackbar.ANIMATION_MODE_SLIDE).show()
             }
         }
        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
         adapter.notifyDataSetChanged()
    }
}