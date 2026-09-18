package mw.dannie.fumigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

private val Blue = Color(0xFF075985)
private val Dark = Color(0xFF061A2A)
private val Green = Color(0xFF39D353)
private val Light = Color(0xFFF4FBF7)
private val Muted = Color(0xFF5C6D78)

data class Service(val icon:String, val name:String, val description:String, val imageRes:Int? = null)
data class Price(val service:String, val six:String, val year:String, val type:String)

val services = listOf(
    Service("🪳","Cockroach Control","Targeted treatment to help control cockroach infestations.",R.drawable.cockroaches),
    Service("🛏️","Bedbug Control","Professional treatment for bedbugs in homes and premises.",R.drawable.bedbugs),
    Service("🐀","Rodent Control","Solutions for rats and mice around residential and commercial spaces."),
    Service("🦟","Mosquito Control","Treatment aimed at reducing mosquito problems.",R.drawable.mosquitoes),
    Service("🐝","Bee Control","Professional assistance with unwanted bee activity."),
    Service("🐍","Snake Control","Practical assistance for snake-related concerns."),
    Service("🦎","Lizard Control","Help managing unwanted lizards around premises."),
    Service("🐜","Ant Control","Targeted treatment to help control ant infestations."),
    Service("🕷️","Tick Control","Professional treatment for areas affected by ticks."),
    Service("🏢","General Pest Control","Customized solutions for common pest problems.")
)

val prices = listOf(
    Price("Bedbugs","K120,000","K180,000","Standard House"), Price("Termites","From K250,000","From K350,000","Standard House"), Price("Cockroach Control","K150,000","K250,000","Standard House"),
    Price("Bedbugs","K150,000","K270,000","Mansion House"), Price("Termites","From K370,000","From K450,000","Mansion House"), Price("Cockroach Control","K180,000","K350,000","Mansion House")
)

fun open(context: Context, url: String) { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
fun wa(context: Context, msg: String) { open(context, "https://wa.me/265885769227?text=" + Uri.encode(msg)) }

@Composable
fun BrandHeader() {
    Column(Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.dannie_logo), "Dannie Fumigation Services logo", Modifier.size(105.dp).clip(RoundedCornerShape(18.dp)), contentScale = ContentScale.Fit)
        Text("PROFESSIONAL PEST CONTROL", 12.sp, FontWeight.Bold, color = Blue, modifier = Modifier.padding(top = 8.dp))
        Text("ONCE FOREVER", 12.sp, FontWeight.ExtraBold, color = Green)
    }
}

@Composable
fun ActionButton(text:String, onClick:()->Unit, filled:Boolean=true) {
    if (filled) Button(onClick, Modifier.fillMaxWidth().height(52.dp), colors=ButtonDefaults.buttonColors(containerColor=Green, contentColor=Dark), shape=RoundedCornerShape(14.dp)) { Text(text, fontWeight=FontWeight.Bold) }
    else OutlinedButton(onClick, Modifier.fillMaxWidth().height(52.dp), shape=RoundedCornerShape(14.dp)) { Text(text, fontWeight=FontWeight.Bold) }
}

@Composable
fun ServiceCard(s:Service, onClick: (() -> Unit)? = null) {
    Card(
        Modifier.fillMaxWidth().padding(vertical=5.dp).then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape=RoundedCornerShape(18.dp),
        colors=CardDefaults.cardColors(containerColor=Color.White)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment=Alignment.CenterVertically) {
            if (s.imageRes != null) {
                Image(
                    painter = painterResource(s.imageRes),
                    contentDescription = s.name,
                    modifier = Modifier.size(78.dp).clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(s.icon, 30.sp, modifier=Modifier.width(45.dp))
            }
            Column(Modifier.padding(start=12.dp)) {
                Text(s.name,18.sp,FontWeight.Bold,color=Blue)
                Text(s.description,14.sp,color=Muted,modifier=Modifier.padding(top=3.dp))
            }
        }
    }
}

@Composable
fun App() {
    val nav=rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route=backStack?.destination?.route ?: "home"
    Scaffold(bottomBar={
        NavigationBar(containerColor=Color.White) {
            listOf("home" to "⌂", "services" to "✚", "prices" to "K", "book" to "✓", "contact" to "☎").forEach { (r,i) ->
                NavigationBarItem(selected=route==r, onClick={nav.navigate(r){launchSingleTop=true}}, icon={Text(i,fontSize=20.sp)}, label={Text(r.replaceFirstChar{it.uppercase()})})
            }
        }
    }) { p -> NavHost(nav,"home",Modifier.padding(p)) {
        composable("home"){Home(nav)}; composable("services"){Services(nav)}; composable("prices"){Prices()}; composable("book"){Book()}; composable("contact"){Contact()}; composable("service/{index}"){entry -> val index=entry.arguments?.getString("index")?.toIntOrNull() ?: 0; ServiceDetail(services.getOrElse(index){services[0]}, nav)}
    }}
}

@Composable
fun Home(nav:NavHostController) {
    val c=LocalContext.current
    LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=16.dp)) {
        item {
            BrandHeader()
            Card(colors=CardDefaults.cardColors(containerColor=Dark), shape=RoundedCornerShape(26.dp), modifier=Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp)) {
                    Text("DANNIE FUMIGATION SERVICES",25.sp,FontWeight.ExtraBold,color=Color.White)
                    Text("Reliable fumigation and pest-control solutions for homes, businesses and other premises.",16.sp,color=Color.White,modifier=Modifier.padding(top=10.dp))
                    Text("Lilongwe • Blantyre • Mzuzu & surrounding areas",14.sp,color=Green,FontWeight.Bold,modifier=Modifier.padding(top=12.dp))
                    Spacer(Modifier.height(18.dp)); ActionButton("BOOK A SERVICE",{nav.navigate("book")})
                    Spacer(Modifier.height(9.dp)); ActionButton("WHATSAPP US",{open(c,"https://wa.me/message/4Y7IDRCI6OZYJ1")},false)
                }
            }
            Spacer(Modifier.height(22.dp)); Text("Our Services",23.sp,FontWeight.Bold,color=Blue); Text("Professional pest-control solutions",14.sp,color=Muted,modifier=Modifier.padding(top=3.dp,bottom=8.dp))
        }
        items(services.take(4)){ServiceCard(it)}
        item { Spacer(Modifier.height(12.dp)); ActionButton("VIEW ALL SERVICES",{nav.navigate("services")},false); Spacer(Modifier.height(20.dp)) }
    }
}

@Composable fun Services(nav:NavHostController){ LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=16.dp)){item{BrandHeader();Text("Our Services",28.sp,FontWeight.Bold,color=Blue);Text("Choose the pest-control service you need.",14.sp,color=Muted,modifier=Modifier.padding(top=4.dp,bottom=10.dp))};items(services.withIndex().toList()){item->ServiceCard(item.value){nav.navigate("service/${item.index}")}}} }

@Composable fun Prices(){ val c=LocalContext.current; LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=16.dp)){item{BrandHeader();Text("Service Prices",28.sp,FontWeight.Bold,color=Blue);Text("Guide prices — final pricing can depend on location and affected area size.",14.sp,color=Muted,modifier=Modifier.padding(vertical=8.dp))};items(listOf("Standard House","Mansion House")){type->Text(type,20.sp,FontWeight.Bold,color=Blue,modifier=Modifier.padding(top=12.dp,bottom=4.dp));prices.filter{it.type==type}.forEach{p->Card(Modifier.fillMaxWidth().padding(vertical=5.dp),shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(Color.White)){Column(Modifier.padding(17.dp)){Text(p.service,18.sp,FontWeight.Bold,color=Dark);Text("6 months: ${p.six}");Text("1 year: ${p.year}");Text("Prices may vary by property and treatment needs.",12.sp,color=Muted,modifier=Modifier.padding(top=4.dp));TextButton({wa(c,"Hello DANNIE FUMIGATION SERVICES, I am interested in ${p.service} for a ${p.type}. Please confirm the current price for my location.")}){Text("GET A QUOTE →",color=Blue,fontWeight=FontWeight.Bold)}}}}};item{Text("Other Services",20.sp,FontWeight.Bold,color=Blue,modifier=Modifier.padding(top=18.dp,bottom=8.dp));val other=listOf("Snake" to "From K120,000","Ants" to "From K150,000","Rodent" to "From K95,000","Ticks" to "From K120,000","Mosquitoes" to "From K250,000","Bees" to "From K120,000","Lizards" to "From K140,000","Spider" to "From K150,000","General Fumigations" to "From K250,000");other.forEach{(s,p)->Card(Modifier.fillMaxWidth().padding(vertical=3.dp),shape=RoundedCornerShape(12.dp),colors=CardDefaults.cardColors(Color.White)){Row(Modifier.fillMaxWidth().padding(14.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(s,15.sp,FontWeight.SemiBold);Text(p,15.sp,FontWeight.Bold,color=Blue)}}};Spacer(Modifier.height(20.dp))}}
}

@Composable
fun Book(){
    val c=LocalContext.current
    var n by remember{mutableStateOf("")}
    var ph by remember{mutableStateOf("")}
    var loc by remember{mutableStateOf("")}
    var svc by remember{mutableStateOf("")}
    var propertyType by remember{mutableStateOf("")}
    var d by remember{mutableStateOf("")}
    var selectedDate by remember{mutableStateOf("")}
    var serviceExpanded by remember{mutableStateOf(false)}
    var propertyExpanded by remember{mutableStateOf(false)}
    val calendar=remember{Calendar.getInstance()}
    val dateFormat=remember{SimpleDateFormat("dd MMM yyyy",Locale.getDefault())}

    LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=20.dp)){
        item{
            BrandHeader()
            Text("Book a Service",28.sp,FontWeight.Bold,color=Blue)
            Text("Tell us what you need and your preferred date. We’ll confirm availability with you.",14.sp,color=Muted,modifier=Modifier.padding(top=4.dp,bottom=14.dp))

            OutlinedTextField(n,{n=it},label={Text("Full name")},modifier=Modifier.fillMaxWidth().padding(vertical=5.dp),shape=RoundedCornerShape(12.dp),singleLine=true)
            OutlinedTextField(ph,{ph=it},label={Text("Phone number")},modifier=Modifier.fillMaxWidth().padding(vertical=5.dp),shape=RoundedCornerShape(12.dp),singleLine=true)
            OutlinedTextField(loc,{loc=it},label={Text("Service location")},placeholder={Text("Area / town / address")},modifier=Modifier.fillMaxWidth().padding(vertical=5.dp),shape=RoundedCornerShape(12.dp),minLines=1)

            Box(Modifier.fillMaxWidth().padding(vertical=5.dp)){
                OutlinedTextField(svc,{},label={Text("Service required")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),readOnly=true,trailingIcon={Text("⌄",fontSize=20.sp)})
                Spacer(Modifier.matchParentSize().clickable{serviceExpanded=true})
                DropdownMenu(expanded=serviceExpanded,onDismissRequest={serviceExpanded=false},modifier=Modifier.fillMaxWidth(.9f)){
                    services.forEach{item->DropdownMenuItem(text={Text(item.name)},onClick={svc=item.name;serviceExpanded=false})}
                }
            }

            Box(Modifier.fillMaxWidth().padding(vertical=5.dp)){
                OutlinedTextField(propertyType,{},label={Text("Property type")},placeholder={Text("Select property type")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp),readOnly=true,trailingIcon={Text("⌄",fontSize=20.sp)})
                Spacer(Modifier.matchParentSize().clickable{propertyExpanded=true})
                DropdownMenu(expanded=propertyExpanded,onDismissRequest={propertyExpanded=false},modifier=Modifier.fillMaxWidth(.9f)){
                    listOf("Standard House","Mansion House","Apartment / Flat","Office / Business","Other").forEach{item->DropdownMenuItem(text={Text(item)},onClick={propertyType=item;propertyExpanded=false})}
                }
            }

            OutlinedTextField(selectedDate,{},label={Text("Preferred service date")},placeholder={Text("Select a date")},modifier=Modifier.fillMaxWidth().padding(vertical=5.dp).clickable{
                val today=Calendar.getInstance()
                DatePickerDialog(c,{_,year,month,day->
                    calendar.set(year,month,day)
                    selectedDate=dateFormat.format(calendar.time)
                },today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH)).apply{
                    datePicker.minDate=today.timeInMillis
                }.show()
            },shape=RoundedCornerShape(12.dp),readOnly=true,trailingIcon={Text("📅")})

            OutlinedTextField(d,{d=it},label={Text("Additional details")},placeholder={Text("Pest problem, number of rooms, preferred time, etc.")},modifier=Modifier.fillMaxWidth().padding(vertical=5.dp),minLines=4,shape=RoundedCornerShape(12.dp))

            Spacer(Modifier.height(10.dp))
            Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(16.dp),colors=CardDefaults.cardColors(containerColor=Color.White)){
                Column(Modifier.padding(16.dp)){
                    Text("Booking summary",17.sp,FontWeight.Bold,color=Blue)
                    Text(if(svc.isBlank()) "Service: Not selected" else "Service: $svc",14.sp,color=Dark,modifier=Modifier.padding(top=7.dp))
                    Text(if(propertyType.isBlank()) "Property: Not selected" else "Property: $propertyType",14.sp,color=Dark)
                    Text(if(selectedDate.isBlank()) "Date: Not selected" else "Date: $selectedDate",14.sp,color=Dark)
                }
            }
            Spacer(Modifier.height(12.dp))
            ActionButton("SEND BOOKING TO WHATSAPP",{
                wa(c,"Hello DANNIE FUMIGATION SERVICES. I would like to book a service.\n\nName: $n\nPhone: $ph\nLocation: $loc\nService: $svc\nProperty type: $propertyType\nPreferred date: $selectedDate\nAdditional details: $d")
            })
            Text("Your request will open in WhatsApp for confirmation. Prices and availability are confirmed by Dannie Fumigation Services.",12.sp,color=Muted,modifier=Modifier.padding(top=8.dp,bottom=24.dp))
        }
    }
}
@Composable
fun ServiceDetail(s: Service, nav: NavHostController) {
    val c=LocalContext.current
    LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=20.dp)) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("← Back to Services",15.sp,FontWeight.Bold,color=Blue,modifier=Modifier.clickable{nav.popBackStack()}.padding(vertical=10.dp))
            BrandHeader()
            Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(containerColor=Dark)){
                Column {
                    if (s.imageRes != null) {
                        Image(
                            painter = painterResource(s.imageRes),
                            contentDescription = s.name,
                            modifier = Modifier.fillMaxWidth().height(220.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(Modifier.padding(24.dp)){
                        Text(s.icon,52.sp)
                        Text(s.name,28.sp,FontWeight.ExtraBold,color=Color.White,modifier=Modifier.padding(top=8.dp))
                        Text(s.description,16.sp,color=Color.White,modifier=Modifier.padding(top=10.dp))
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Text("Professional treatment",21.sp,FontWeight.Bold,color=Blue)
            Text("We assess the affected area and recommend a suitable treatment approach based on the pest problem, property and site conditions. Contact Dannie Fumigation Services to discuss your requirements and confirm availability.",15.sp,color=Muted,modifier=Modifier.padding(top=6.dp))
            Spacer(Modifier.height(18.dp))
            ActionButton("BOOK THIS SERVICE",{nav.navigate("book")})
            Spacer(Modifier.height(10.dp))
            ActionButton("WHATSAPP FOR A QUOTE",{wa(c,"Hello DANNIE FUMIGATION SERVICES. I am interested in ${s.name}. Please advise on availability and the current price for my location.")},false)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable fun Contact(){val c=LocalContext.current;LazyColumn(Modifier.fillMaxSize().background(Light).padding(horizontal=20.dp),horizontalAlignment=Alignment.CenterHorizontally){item{BrandHeader();Text("Contact Us",30.sp,FontWeight.Bold,color=Blue);Text("We’re ready to help with pest-control enquiries and service arrangements.",14.sp,color=Muted,modifier=Modifier.padding(8.dp));Spacer(Modifier.height(10.dp));ActionButton("💬  CHAT ON WHATSAPP",{open(c,"https://wa.me/message/4Y7IDRCI6OZYJ1")});Spacer(Modifier.height(10.dp));ActionButton("📞  CALL +265 885 769 227",{c.startActivity(Intent(Intent.ACTION_DIAL,Uri.parse("tel:+265885769227")))},false);Spacer(Modifier.height(10.dp));ActionButton("📘  FACEBOOK",{open(c,"https://www.facebook.com/share/1GZbLmiDAE/")},false);Spacer(Modifier.height(24.dp));Text("Lilongwe • Blantyre • Mzuzu",17.sp,FontWeight.Bold,color=Dark);Text("& surrounding areas",14.sp,color=Muted);Text("ONCE FOREVER",15.sp,FontWeight.ExtraBold,color=Green,modifier=Modifier.padding(top=8.dp))}}
}

class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b); androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen(); setContent{MaterialTheme(colorScheme=lightColorScheme(primary=Blue,secondary=Green,background=Light)){Surface(color=Light){App()}}}}}
