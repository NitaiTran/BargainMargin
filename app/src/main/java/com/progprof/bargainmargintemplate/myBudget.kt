package com.progprof.bargainmargintemplate

class myBudget(var initialBudgetLeft: Int, var initialBudget: Int)
{
    var remainingBudget = initialBudgetLeft
    var totalBudget = initialBudget
        private set


    fun updateRemainingBudget(initialBudgetLeft: Int)
    {
        remainingBudget = initialBudgetLeft
    }

    fun updateTotalBudget(initialBudget: Int)
    {
        totalBudget = initialBudget
    }



}



