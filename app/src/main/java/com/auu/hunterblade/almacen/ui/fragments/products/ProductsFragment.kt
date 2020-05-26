package com.auu.hunterblade.almacen.ui.fragments.products

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.auu.hunterblade.almacen.BuildConfig
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.data.Product
import com.auu.hunterblade.almacen.databinding.FragmentProductsBinding
import com.auu.hunterblade.almacen.ui.adapters.ListProdsAdapter
import com.auu.hunterblade.almacen.utils.InjectorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

class ProductsFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_TAKE_PHOTO = 200
    private val CLICK_IMAGE_REQUEST = 2

    private var mCurrentPhotoPath: String? = null

    private val _url = MutableLiveData<String>().apply {
        value = ""
    }
    private val urlPhoto: LiveData<String> = _url

    private val viewModel: ProductViewModel by viewModels {
        InjectorUtils.provideProductViewModelFactory(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProductsBinding.inflate(inflater, container, false)

        context ?: return binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.products)
        val addProd = view.findViewById<FloatingActionButton>(R.id.addProd)

        val adapter = ListProdsAdapter(viewModel)
        recycler.adapter = adapter
        subscribeUi(adapter)

        addProd.setOnClickListener { p ->

            val d = Dialog(requireActivity())
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.product_dialog_insert)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(d.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            d.window!!.attributes = lp
            d.show()

            val name: EditText = d.findViewById(R.id.etProductName)
            val description: EditText = d.findViewById(R.id.etProductDecription)
            val priceBuy: EditText = d.findViewById(R.id.priceBuy)
            val priceSell: EditText = d.findViewById(R.id.priceSell)
            val amount: EditText = d.findViewById(R.id.amount)
            val acceptProduct = d.findViewById<Button>(R.id.acceptProduct)
            val cancelProduct = d.findViewById<Button>(R.id.cancelProduct)
            val photo = d.findViewById<ImageView>(R.id.ibPhoto)

            val gallery = d.findViewById<ImageButton>(R.id.ibModifyGallery)
            val camara = d.findViewById<ImageButton>(R.id.modifyPhoto)

//            name.requestFocus()
            showKeyboard(name)

            fun Validador(): Boolean {
                var validado = true

                if (amount.text.toString().isEmpty()) {
                    validado = false
                    amount.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_amount),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (priceSell.text.toString().isEmpty()) {
                    validado = false
                    priceSell.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_price),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (priceBuy.text.toString().isEmpty()) {
                    validado = false
                    priceBuy.requestFocus()
                    Toast.makeText(
                        context, getString(R.string.alert_empty_price),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (description.text.toString().isEmpty()) {
                    validado = false
                    description.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_description),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (name.text.toString().isEmpty()) {
                    validado = false
                    name.requestFocus()
                    Toast.makeText(
                        context,
                        getString(R.string.alert_empty_name),
                        Toast.LENGTH_SHORT
                    ).show()
                }


                return validado
            }

            urlPhoto.observe(viewLifecycleOwner){

                if(it != ""){
                    Glide.with(p.context)
                        .load(Uri.parse("file:///$it"))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(photo)
                }else {

                    Glide.with(p.context)
                        .load(R.drawable.ic_crop_original)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(photo)
                }

            }

            camara.setOnClickListener {
                verifyPermissionTakePicture()
            }
            gallery.setOnClickListener {
                verifyPermissionPickFromGallery()
            }


            cancelProduct.setOnClickListener { d.dismiss() }

            acceptProduct.setOnClickListener {

                if(Validador()){

                    if(mCurrentPhotoPath != null && mCurrentPhotoPath != ""){
                        val prod = Product(name.text.toString(), description.text.toString(), priceBuy.text.toString().toFloat(), priceSell.text.toString().toFloat(), mCurrentPhotoPath!!, amount.text.toString().toLong())

                        viewModel.addProduct(prod)
                        mCurrentPhotoPath = ""
                        _url.apply {
                            value = mCurrentPhotoPath
                        }
                        d.dismiss()
                    }else {
                        val prod = Product(name.text.toString(), description.text.toString(), priceBuy.text.toString().toFloat(), priceSell.text.toString().toFloat(), "", amount.text.toString().toLong())

                        viewModel.addProduct(prod)

                        d.dismiss()
                    }

                }

            }

        }

    }

    private fun subscribeUi(adapter: ListProdsAdapter) {
        viewModel.lista.observe(viewLifecycleOwner){ list ->
            adapter.submitList(list)
        }
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

    fun getRealPathFromURI(uri: Uri): String? {
        val filePathColumn =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        var picturePath: String? = null
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex)
            cursor.close()
        }
        return picturePath
    }

    fun compressPicture(){

        fun calculateInSampleSize(options: BitmapFactory.Options,  reqWidth: Int, reqHeight: Int): Int {
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri? = data.data
            if(uri != null){
                val finalFile = File(getRealPathFromURI(uri)!!)
                mCurrentPhotoPath = finalFile.absolutePath
                _url.apply {
                    value = mCurrentPhotoPath
                }
            }


//            val intent = Intent(this@M, detailedActivity::class.java)
//            intent.putExtra("PATH", finalFile.path)
//            startActivity(intent)

            Log.d(TAG,"PICK_IMAGE_REQUEST")
        } else if (requestCode == CLICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            CoroutineScope(Dispatchers.IO).launch {
                compressPicture()
            }
            _url.apply {
                value = mCurrentPhotoPath
            }
//            val d = data
//            galleryAddPic()
//            val intent = Intent(this@IdentifyActivity, detailedActivity::class.java)
//            intent.putExtra("PATH", mCurrentPhotoPath)
//            startActivity(intent)
            Log.d(TAG,"CLICK_IMAGE_REQUEST")
        }
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun verifyPermissionPickFromGallery() { //WRITE_EXTERNAL_STORAGE tiene implícito READ_EXTERNAL_STORAGE
//porque pertenecen al mismo grupo de permisos
        val PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        var writePermission = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            writePermission = checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarMultiplesPermiso(PERMISSIONS, "Sin el permiso" +
                    "   administrar ficheros no puede copiar los datos.",
                PICK_IMAGE_REQUEST, requireActivity())
        } else {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
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
            writePermission = checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            solicitarMultiplesPermiso(PERMISSIONS, "Sin el permiso" +
                    "   administrar ficheros no puede copiar los datos.",
                REQUEST_TAKE_PHOTO, requireActivity())
        } else {
            dispatchTakePictureIntent()
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

    companion object {
        private const val TAG = "ProductFragment"
    }
}