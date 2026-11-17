package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private data class Word(val es:String,val en:String,val emoji:String,val group:String)
private val words = listOf(
 Word("Manzana","Apple","🍎","Frutas"),Word("Plátano","Banana","🍌","Frutas"),Word("Uva","Grape","🍇","Frutas"),Word("Naranja","Orange","🍊","Frutas"),Word("Fresa","Strawberry","🍓","Frutas"),
 Word("Mesa","Table","🪑","Casa"),Word("Silla","Chair","🪑","Casa"),Word("Ventana","Window","🪟","Casa"),Word("Puerta","Door","🚪","Casa"),Word("Cama","Bed","🛏️","Casa"),
 Word("Gato","Cat","🐱","Animales"),Word("Perro","Dog","🐶","Animales"),Word("Pájaro","Bird","🐦","Animales"),Word("Pez","Fish","🐟","Animales"),Word("León","Lion","🦁","Animales"),
 Word("Mariposa","Butterfly","🦋","Insectos"),Word("Hormiga","Ant","🐜","Insectos"),Word("Abeja","Bee","🐝","Insectos"),Word("Mosquito","Mosquito","🦟","Insectos")
)
class MainActivity: ComponentActivity() {
 private var tts:TextToSpeech?=null
 override fun onCreate(savedInstanceState:Bundle?) { super.onCreate(savedInstanceState)
  tts=TextToSpeech(this) { status -> if(status==TextToSpeech.SUCCESS) tts?.language=Locale("es","ES") }
  setContent { DosMundos { text, english ->
   tts?.language=if(english) Locale.US else Locale("es","ES")
   tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"word")
  } }
 }
 override fun onDestroy(){tts?.stop();tts?.shutdown();super.onDestroy()}
}
@Composable private fun DosMundos(speak:(String,Boolean)->Unit){
 var tab by remember { mutableIntStateOf(0) }; var stars by remember { mutableIntStateOf(0) }
 val tabs=listOf("Inicio","Explorar","Preguntas","Memoria","Palabras","Estrellas")
 MaterialTheme(colorScheme=lightColorScheme(primary=Color(0xFF3859C7),secondary=Color(0xFF178A76))) {
 Scaffold(bottomBar={ NavigationBar { tabs.forEachIndexed { i,t -> NavigationBarItem(selected=tab==i,onClick={tab=i},icon={Text(listOf("🏠","📚","❓","🧠","🔤","⭐")[i])},label={Text(t,fontSize=9.sp)}) } } }) { padding ->
 Column(Modifier.fillMaxSize().padding(padding).padding(12.dp)) {
 Text("Two Worlds | Dos Mundos",fontSize=23.sp,fontWeight=FontWeight.Bold,color=Color(0xFF3859C7))
 Text("Aprendemos español e inglés · ⭐ $stars",fontSize=13.sp)
 Spacer(Modifier.height(12.dp))
 when(tab){
 0 -> { Text("¡Hola! Hello! 👋",fontSize=28.sp); Text("Explora palabras, juega y aprende en dos idiomas."); Spacer(Modifier.height(16.dp)); Button(onClick={tab=1}){Text("Comenzar / Start")}; Text("🦎 Tu amigo ajolote te acompaña",fontSize=20.sp) }
 1 -> Explore(speak)
 2 -> Quiz(speak){stars+=it}
 3 -> Memory {stars+=it}
 4 -> Builder(speak){stars+=it}
 else -> {Text("Tus estrellas: ⭐ $stars",fontSize=25.sp);Text("¡Cada palabra nueva cuenta! / Every word matters!");Text("Consejo para familias: practiquen cinco palabras cada día en ambos idiomas.")}
 }
 } }
 }
}
@Composable private fun Explore(speak:(String,Boolean)->Unit){
 var search by remember { mutableStateOf("") };var category by remember { mutableStateOf("Todas") }
 OutlinedTextField(search,{search=it},label={Text("Buscar / Search")},modifier=Modifier.fillMaxWidth(),singleLine=true)
 Row(horizontalArrangement=Arrangement.spacedBy(4.dp)) { listOf("Todas","Frutas","Casa","Animales","Insectos").forEach { name -> TextButton(onClick={category=name},contentPadding=PaddingValues(2.dp)){Text(name,fontSize=10.sp)} } }
 LazyColumn(verticalArrangement=Arrangement.spacedBy(7.dp)) {items(words.filter{(category=="Todas"||it.group==category)&&(it.es.contains(search,true)||it.en.contains(search,true))}){word ->
 Card(Modifier.fillMaxWidth()){Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically){Text(word.emoji,fontSize=32.sp);Spacer(Modifier.width(12.dp));Column(Modifier.weight(1f)){Text(word.es,fontWeight=FontWeight.Bold);Text(word.en)};TextButton(onClick={speak(word.es,false)}){Text("ES 🔊")};TextButton(onClick={speak(word.en,true)}){Text("EN 🔊")} } }
 }}
}
@Composable private fun Quiz(speak:(String,Boolean)->Unit,award:(Int)->Unit){
 var number by remember {mutableIntStateOf(0)};var chosen by remember {mutableStateOf<String?>(null)}
 val word=words[number%words.size];val options=remember(number){(listOf(word)+words.filter{it!=word}.shuffled().take(3)).shuffled()}
 Text("¿Cómo se dice en inglés?",fontSize=21.sp);Text("${word.emoji} ${word.es}",fontSize=30.sp)
 TextButton(onClick={speak(word.es,false)}){Text("Escuchar 🔊")}
 options.forEach { opt -> OutlinedButton(onClick={if(chosen==null){chosen=opt.en;if(opt==word)award(1)}},modifier=Modifier.fillMaxWidth()){Text(opt.en)} }
 if(chosen!=null){Text(if(chosen==word.en)"¡Correcto! ⭐" else "Respuesta: ${word.en}");Button(onClick={number++;chosen=null}){Text("Siguiente / Next")}}
}
@Composable private fun Memory(award:(Int)->Unit){
 var round by remember {mutableIntStateOf(0)};val pair=words[round%words.size]
 var selected by remember(round){mutableStateOf<String?>(null)};var matched by remember(round){mutableStateOf(false)}
 val choices=remember(round){listOf(pair.en,pair.es,words[(round+3)%words.size].en).shuffled()}
 Text("Encuentra la pareja / Match",fontSize=22.sp);Text("${pair.emoji} ${pair.es}",fontSize=28.sp)
 choices.forEach{option->OutlinedButton(onClick={selected=option;if(option==pair.en&&!matched){matched=true;award(1)}},modifier=Modifier.fillMaxWidth()){Text(option)}}
 if(selected!=null)Text(if(matched)"¡Pareja encontrada! ⭐" else "Inténtalo otra vez")
 if(matched)Button(onClick={round++}){Text("Otra pareja")}
}
@Composable private fun Builder(speak:(String,Boolean)->Unit,award:(Int)->Unit){
 var index by remember {mutableIntStateOf(0)};var entry by remember(index){mutableStateOf("")};var won by remember(index){mutableStateOf(false)}
 val word=words[index%words.size]
 Text("Forma la palabra / Spell it",fontSize=22.sp);Text("${word.emoji} ${word.es}",fontSize=29.sp)
 TextButton(onClick={speak(word.en,true)}){Text("Escuchar inglés 🔊")}
 OutlinedTextField(entry,{entry=it},label={Text("Escribe en inglés")},singleLine=true)
 Button(onClick={if(entry.trim().equals(word.en,true)&&!won){won=true;award(1)}}){Text("Comprobar")}
 if(won){Text("¡Excelente! ⭐");Button(onClick={index++}){Text("Siguiente")}}
 else if(entry.isNotBlank())Text("Pista: empieza con ${word.en.first()}")
}
