package com.pureblacksoft.creepyone.function

import android.util.TypedValue
import android.widget.TextView
import com.airbnb.paris.utils.getFont
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.application.CreepyoneApp

class PrefFun
{
    companion object {
        //region Font Id
        const val ID_FONT_ARIAL = 0
        const val ID_FONT_GEORGIA = 1
        const val ID_FONT_COMFORTAA = 2
        const val ID_FONT_MONTSERRAT = 3
        //endregion

        //region Font Size Id
        const val ID_FONT_SIZE_S = 1
        const val ID_FONT_SIZE_M = 2
        const val ID_FONT_SIZE_L = 3
        const val ID_FONT_SIZE_XL = 4
        //endregion

        var currentFont = ID_FONT_ARIAL
        var currentFontSize = ID_FONT_SIZE_M

        fun setStoryFont(tvTitle: TextView, tvContent: TextView, tvFooter: TextView, tvFooterLabel: TextView) {
            val appContext = CreepyoneApp.getAppContext()

            //region Font Control
            val contentFont = when (currentFont) {
                ID_FONT_GEORGIA -> R.font.georgia
                ID_FONT_COMFORTAA -> R.font.comfortaa
                ID_FONT_MONTSERRAT -> R.font.montserrat
                else -> R.font.arial
            }

            tvContent.typeface = appContext.getFont(contentFont)
            //endregion

            //region Font Size Control
            val titleFontSize: Int
            val contentFontSize: Int
            val footerFontSize: Int

            when (currentFontSize) {
                ID_FONT_SIZE_S -> {
                    titleFontSize = R.dimen.Story_Title_TextSize_S
                    contentFontSize = R.dimen.Story_Content_TextSize_S
                    footerFontSize = R.dimen.Story_Footer_TextSize_S
                }
                ID_FONT_SIZE_M -> {
                    titleFontSize = R.dimen.Story_Title_TextSize_M
                    contentFontSize = R.dimen.Story_Content_TextSize_M
                    footerFontSize = R.dimen.Story_Footer_TextSize_M
                }
                ID_FONT_SIZE_L -> {
                    titleFontSize = R.dimen.Story_Title_TextSize_L
                    contentFontSize = R.dimen.Story_Content_TextSize_L
                    footerFontSize = R.dimen.Story_Footer_TextSize_L
                }
                ID_FONT_SIZE_XL -> {
                    titleFontSize = R.dimen.Story_Title_TextSize_XL
                    contentFontSize = R.dimen.Story_Content_TextSize_XL
                    footerFontSize = R.dimen.Story_Footer_TextSize_XL
                }
                else -> {
                    when (appContext.resources.configuration.fontScale) {
                        0.85f -> {
                            currentFontSize = ID_FONT_SIZE_S
                            titleFontSize = R.dimen.Story_Title_TextSize_S
                            contentFontSize = R.dimen.Story_Content_TextSize_S
                            footerFontSize = R.dimen.Story_Footer_TextSize_S
                        }
                        1.15f -> {
                            currentFontSize = ID_FONT_SIZE_L
                            titleFontSize = R.dimen.Story_Title_TextSize_L
                            contentFontSize = R.dimen.Story_Content_TextSize_L
                            footerFontSize = R.dimen.Story_Footer_TextSize_L
                        }
                        1.30f -> {
                            currentFontSize = ID_FONT_SIZE_XL
                            titleFontSize = R.dimen.Story_Title_TextSize_XL
                            contentFontSize = R.dimen.Story_Content_TextSize_XL
                            footerFontSize = R.dimen.Story_Footer_TextSize_XL
                        }
                        else -> {
                            currentFontSize = ID_FONT_SIZE_M
                            titleFontSize = R.dimen.Story_Title_TextSize_M
                            contentFontSize = R.dimen.Story_Content_TextSize_M
                            footerFontSize = R.dimen.Story_Footer_TextSize_M
                        }
                    }
                }
            }

            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, appContext.resources.getDimension(titleFontSize))
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, appContext.resources.getDimension(contentFontSize))
            tvFooter.setTextSize(TypedValue.COMPLEX_UNIT_PX, appContext.resources.getDimension(footerFontSize))
            tvFooterLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, appContext.resources.getDimension(footerFontSize))
            //endregion
        }
    }
}

//PureBlack Software / Murat BIYIK