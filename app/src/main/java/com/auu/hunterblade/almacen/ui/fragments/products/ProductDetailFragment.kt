package com.auu.hunterblade.almacen.ui.fragments.products

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.auu.hunterblade.almacen.BuildConfig
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentProductDetailBinding
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class ProductDetailFragment : Fragment() {

    private val args: ProductDetailFragmentArgs by navArgs()

    private val viewModelP: ProductDetailViewModel by viewModels {
        InjectorUtils.provideProductDetailViewModelFactory(requireActivity(), args.id)
    }

    private val PICK_IMAGE_REQUEST = 3
    private val REQUEST_TAKE_PHOTO = 300
    private val CLICK_IMAGE_REQUEST = 4

    private var mCurrentPhotoPath: String? = null

    private val _url = MutableLiveData<String>().apply {
        value = ""
    }
    private val urlPhoto: LiveData<String> = _url

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentProductDetailBinding>(
            inflater, R.layout.fragment_product_detail, container, false
        ).apply {
            viewModel = viewModelP
            lifecycleOwner = viewLifecycleOwner

            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_share -> {
                        createShareIntent()
                        true
                    }
                    else -> false
                }
            }
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modifyPhoto = view.findViewById<ImageButton>(R.id.modifyPhoto)
        val detailImage = view.findViewById<ImageView>(R.id.detail_image)

        val modify = view.findViewById<ImageButton>(R.id.ibModifyP)
        val editName = view.findViewById<EditText>(R.id.etProduct_detail_name)
        val name = view.findViewById<TextView>(R.id.product_detail_name)
        val editDescription = view.findViewById<EditText>(R.id.etDescription)
        val description = view.findViewById<TextView>(R.id.description)
        val editBuyPrice = view.findViewById<EditText>(R.id.edProducto_item_BuyPrice)
        val buyPrice = view.findViewById<TextView>(R.id.producto_item_BuyPrice)
        val editSellPrice = view.findViewById<EditText>(R.id.etProducto_item_SellPrice)
        val sellPrice = view.findViewById<TextView>(R.id.producto_item_SellPrice)
        val editAmount = view.findViewById<EditText>(R.id.etProducto_item_amount)
        val amount = view.findViewById<TextView>(R.id.producto_item_amount)

        urlPhoto.observe(viewLifecycleOwner){

            if(it != ""){
                Glide.with(requireContext())
                    .load(Uri.parse("file:///$it"))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(detailImage)
            }else {

                Glide.with(requireContext())
                    .load(R.drawable.ic_crop_original)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(detailImage)
            }

        }

        viewModelP.product.observe(viewLifecycleOwner){ p ->

            modify.setOnClickListener {

                if(editName.visibility == View.INVISIBLE){
                    modify.setImageResource(R.drawable.ic_done)
                    name.visibility = View.INVISIBLE
                    description.visibility = View.INVISIBLE
                    buyPrice.visibility = View.INVISIBLE
                    sellPrice.visibility = View.INVISIBLE
                    amount.visibility = View.INVISIBLE

                    editName.visibility = View.VISIBLE
                    editName.setText(p.name)

                    editDescription.visibility = View.VISIBLE
                    editDescription.setText(p.description)

                    editBuyPrice.visibility = View.VISIBLE
                    editBuyPrice.setText(p.priceBuy.toString())

                    editSellPrice.visibility = View.VISIBLE
                    editSellPrice.setText(p.priceSell.toString())

                    editAmount.visibility = View.VISIBLE
                    editAmount.setText(p.amount.toString())

                }
                else {

                    when {
                        editName.isFocused -> {
                            hideKeyboard(editName)
                            editName.clearFocus()
                        }
                        editDescription.isFocused -> {
                            hideKeyboard(editDescription)
                            editDescription.clearFocus()
                        }
                        editBuyPrice.isFocused -> {
                            hideKeyboard(editBuyPrice)
                            editBuyPrice.clearFocus()
                        }
                        editSellPrice.isFocused -> {
                            hideKeyboard(editSellPrice)
                            editSellPrice.clearFocus()
                        }
                        editAmount.isFocused -> {
                            hideKeyboard(editAmount)
                            editAmount.clearFocus()
                        }
                    }

                    modify.setImageResource(R.drawable.ic_modify)
                    p.name = editName.text.toString()
                    p.description = editDescription.text.toString()
                    p.priceBuy = editBuyPrice.text.toString().toFloat()
                    p.priceSell = editSellPrice.text.toString().toFloat()
                    p.amount = editAmount.text.toString().toLong()

                    viewModelP.updateProduct(p)

                    editName.visibility = View.INVISIBLE
                    editDescription.visibility = View.INVISIBLE
                    editBuyPrice.visibility = View.INVISIBLE
                    editSellPrice.visibility = View.INVISIBLE
                    editAmount.visibility = View.INVISIBLE

                    name.visibility = View.VISIBLE
                    description.visibility = View.VISIBLE
                    buyPrice.visibility = View.VISIBLE
                    sellPrice.visibility = View.VISIBLE
                    amount.visibility = View.VISIBLE

                }

            }

        }

        modifyPhoto.setOnClickListener {
            verifyPermissionTakePicture()
        }
    }

    private fun verifyPermissionTakePicture() { //WRITE_EXTERNAL_STORAGE tiene implícito READ_EXTERNAL_STORAGE
//porque pertenecen al mismo grupo de permisos
        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        var writePermission = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            writePermission = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarMultiplesPermiso(PERMISSIONS, "Sin el permiso" +
                    "   administrar ficheros no puede copiar los datos.",
                REQUEST_TAKE_PHOTO, requireActivity())
        } else {
            dispatchTakePictureIntent()
        }
    }

    //Creating a path for the captured image to be saved. Using this path we can retrieve the original Image.
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir: File = requireContext().getExternalFilesDir(null)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    // Adding the pic to the gallery so that it is visible to everyone
    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath!!)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        requireActivity().sendBroadcast(mediaScanIntent)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.e(TAG, "Error while creating the file")
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, CLICK_IMAGE_REQUEST)
            }
        }
    }

    fun compressPicture(){

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height: Int = options.outHeight
            val width: Int = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
                val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
                inSampleSize = if(heightRatio < widthRatio) {
                    heightRatio}
                else {
                    widthRatio
                }
            }
            val totalPixels = width * height;
            val totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize;
        }

        val maxWidth = 840.0f
        val maxHeight = 840.0f

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(mCurrentPhotoPath!!, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio: Float = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        var scaledBitmap: Bitmap? = null

        try {
            bmp = BitmapFactory.decodeFile(mCurrentPhotoPath!!, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        bmp.recycle()

        val exif: ExifInterface
        try {
            exif = ExifInterface(mCurrentPhotoPath!!)
            val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90.0f)
            } else if (orientation == 3) {
                matrix.postRotate(180.0f)
            } else if (orientation == 8) {
                matrix.postRotate(270.0f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
//        val filepath: String = getFilename()
        try {
            out = FileOutputStream(mCurrentPhotoPath!!)

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            val uri: Uri? = data.data
//            val finalFile = File(getRealPathFromURI(uri))
//            val intent = Intent(this@M, detailedActivity::class.java)
//            intent.putExtra("PATH", finalFile.path)
//            startActivity(intent)

            Log.d(TAG,"PICK_IMAGE_REQUEST")
        } else if (requestCode == CLICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            CoroutineScope(Dispatchers.IO).launch {
                compressPicture()
            }
            _url.apply {
                value = mCurrentPhotoPath
            }

            viewModelP.product.observe(viewLifecycleOwner){ p ->

                p.photo = if(mCurrentPhotoPath != null && mCurrentPhotoPath != ""){
                    mCurrentPhotoPath!!
                }else { "" }

                viewModelP.updateProduct(p)

            }

//            val d = data
//            galleryAddPic()
//            val intent = Intent(this@IdentifyActivity, detailedActivity::class.java)
//            intent.putExtra("PATH", mCurrentPhotoPath)
//            startActivity(intent)
            Log.d(TAG,"CLICK_IMAGE_REQUEST")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        Log.d(TAG, "onRequestPermissionsResult")
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (grantResults.size == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(requireContext(), "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.size == 2 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                Toast.makeText(requireContext(), "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun solicitarMultiplesPermiso(PERMISSIONS: Array<String>, justificacion: String?, requestCode: Int, actividad: Activity?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad!!,
                PERMISSIONS[0]) && ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                PERMISSIONS[1])) {
            AlertDialog.Builder(actividad)
                .setTitle(getString(R.string.request_permision))
                .setMessage(justificacion)
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(actividad,
                        PERMISSIONS, requestCode)
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(actividad,
                PERMISSIONS, requestCode)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
//        val shareText = plantDetailViewModel.plant.value.let { plant ->
//            if (plant == null) {
//                ""
//            } else {
//                getString(R.string.share_text_plant, plant.name)
//            }
//        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText("shareText")
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    companion object {
        private const val TAG = "ProductDetailFragment"
    }
}