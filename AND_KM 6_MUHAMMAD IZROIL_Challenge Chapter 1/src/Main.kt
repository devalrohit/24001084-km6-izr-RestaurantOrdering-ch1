import datasource.FoodMenuDataSource
import app.utils.IOUtils
import kotlin.concurrent.thread

interface FoodDelivery {
    fun deliverFood()
}

class TakeAwayDelivery : FoodDelivery {
    override fun deliverFood() {
        println("==========================================================")
        println("Makananmu sedang dimasak (5 detik)")
        thread { delayAndPrint("Pesanan selesai(3 detik)!", 5) }
        thread { delayAndPrint("Driver sampai! Pesanan selesai!(3 detik)", 3) }
    }
}

class DeliveryDelivery : FoodDelivery {
    override fun deliverFood() {
        println("==========================================================")
        println("Makananmu sedang dimasak (5 detik)")
        thread { delayAndPrint("Driver sampai! Pesanan selesai!(3 detik)", 5) }
        thread { delayAndPrint("Makananmu sudah siap! Driver segera menuju tempatmu!(5 detik)", 3) }
    }
}

class RestaurantOrdering {
    private val foodMenu = FoodMenuDataSource.foodMenu

    fun displayMenu() {
        println("List Menu Makanan")
        println("-----------------")
        foodMenu.forEachIndexed { index, (menu, harga) ->
            println("${index + 1}. $menu = Rp $harga")
        }
        println("-----------------")
    }

    fun chooseMenu(): Pair<String, Int>? {
        var nomorMenu: Int
        do {
            println("Pilih Menu Makanan (1/2/3/4/5): ")
            nomorMenu = IOUtils.getInputInteger() ?: 0
            if (nomorMenu !in 1..foodMenu.size) {
                println("Menu tidak valid")
            }
        } while (nomorMenu !in 1..foodMenu.size)
        return foodMenu.getOrNull(nomorMenu - 1)?.let { (namaMenu, hargaMenu) ->
            println("Kamu memilih menu $nomorMenu")
            println("Nama menu : $namaMenu")
            println("Harga : Rp.$hargaMenu\n")
            namaMenu to hargaMenu
        }
    }

    private fun processPayment(hargaMenu: Int): Int {
        var pembayaran: Int
        do {
            print("Masukkan Pembayaran: \n")
            pembayaran = IOUtils.getInputInteger() ?: 0
            if (pembayaran < hargaMenu) {
                println(
                    "Maaf, pembayaran kamu gagal!\n" +
                            "Uang kurang buat bayar."
                )
            }
        } while (pembayaran < hargaMenu)
        val kembalian = pembayaran - hargaMenu
        println("==========================================================")
        println(
            "Terima kasih, kamu berhasil memesan makanan.\nKamu dapat kembalian: " +
                    "Rp.$kembalian\n"
        )
        return pembayaran
    }

    fun chooseDeliveryMethod(): FoodDelivery {
        println("Metode Pengiriman Makanan:")
        println("1. Take Away")
        println("2. Delivery")
        var metodePengiriman: Int
        do {
            print("Mau pilih metode pengiriman yang mana? (masukkan nomor): ")
            metodePengiriman = IOUtils.getInputInteger() ?: 0
            if (metodePengiriman !in 1..2) {
                println("Inputannya salah, mohon ulangi.")
            }
        } while (metodePengiriman !in 1..2)
        return when (metodePengiriman) {
            1 -> TakeAwayDelivery()
            2 -> DeliveryDelivery()
            else -> throw IllegalArgumentException("Metode pengiriman tidak valid")
        }
    }


    fun run() {
        displayMenu()
        val menu = chooseMenu() ?: return
        processPayment(menu.second)
        val deliveryMethod = chooseDeliveryMethod()
        deliveryMethod.deliverFood()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            RestaurantOrdering().run()
        }
    }
}

private fun delayAndPrint(message: String, seconds: Long) {
    Thread.sleep(seconds * 1000)
    println(message)
}
