package com.example.digigoat_news.berita

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.digigoat_news.ArticlePageScreen
import com.example.digigoat_news.R
import com.kwabenaberko.newsapilib.models.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsPage(newsViewModel: NewsViewModel, navController: NavHostController) {
    val articles by newsViewModel.article.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            modifier = Modifier
                .height(90.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4B3300),
                titleContentColor = Color.White
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_1),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .width(135.dp)
                            .height(50.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(18.dp))
                }
            },
            actions = {
                IconButton(
                    onClick = {
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.notifikasi),
                        contentDescription = "Notification",
                        modifier = Modifier
                            .size(22.dp)
                            .padding(1.dp),
                        tint = Color.White
                    )
                }
            }
        )
        SearchIcon(newsViewModel)
//        CategoriesBar(newsViewModel)
        Text(
            text = "Berita",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(4.dp)
        )
        JumlahBerita(10)
        SortDropdown { sortBy ->
            newsViewModel.fetchEverythingWithQuery(query = "", sortBy = sortBy)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(articles) {article ->
                ArticleItem(article, navController)
            }
        }
        FooterNavigationBar(navController = navController)
    }
}

@Composable
fun ArticleItem(article: Article, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults
            .cardElevation(
                defaultElevation = 4.dp
            ),
        onClick = {
            navController.navigate(ArticlePageScreen(article.url))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage?: R.drawable.img,
                contentDescription = "Image",
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3
                )
                Text(
                    text = article.source.name,
                    maxLines = 1,
                    fontSize = 14.sp
                )
            }
        }
    }
}

//@Composable
//fun CategoriesBar(newsViewModel: NewsViewModel){
//    val categoriesList = listOf(
//        "General",
//        "Technology",
//        "Business",
//        "Health"
//    )
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .horizontalScroll(rememberScrollState()),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        categoriesList.forEach { category ->
//            Button(
//                onClick = {
//                    newsViewModel.fetchNewsTopHeadline(category)
//                },
//                modifier = Modifier
//                    .padding(4.dp)
//            ) {
//                Text(
//                    text = category
//                )
//            }
//        }
//    }
//}

@Composable
fun SearchIcon(newsViewModel: NewsViewModel){

    var searchQuery by remember {
        mutableStateOf("")
    }
    var isSearchExpanded by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(isSearchExpanded) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(50.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clip(CircleShape),
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isSearchExpanded = false
                            if(searchQuery.isNotEmpty()){
                                newsViewModel.fetchEverythingWithQuery(searchQuery)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon")
                    }
                }
            )
        } else {
            IconButton(
                onClick = {
                    isSearchExpanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon")
            }
        }
    }
}

@Composable
fun JumlahBerita(totalResults: Int) {
    Text(
        text = "$totalResults Berita ditemukan",
        modifier = Modifier
            .padding(4.dp))
}

@Composable
fun SortDropdown(onSortSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Sort by: Terbaru") }

    val options = listOf("Terbaru" to "publishedAt", "Terpopuler" to "popularity", "Relevansi" to "relevancy")

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (label, value) ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = "Sort by: $label"
                        expanded = false
                        onSortSelected(value)
                    },
                    text = { Text(label) }
                )
            }
        }
    }
}

@Composable
fun FooterNavigationBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4B3300))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterItem(iconId = R.drawable.home, title = "Beranda", onClick = { navController.navigate("home") })
        FooterItem(iconId = R.drawable.manajemen, title = "Manajemen", onClick = { navController.navigate("management") })
        FooterItem(iconId = R.drawable.berita, title = "Berita", selected = true, onClick = { navController.navigate("NewsPage") })
        FooterItem(iconId = R.drawable.profil, title = "Profile", onClick = { navController.navigate("profil") })
    }
}

@Composable
fun FooterItem(iconId: Int, title: String, selected: Boolean = false, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() } // Handle Footer Navigation Click
            .padding(4.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = title,
            tint = if (selected) Color(0xFFFBC02D) else Color.White,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color(0xFFFBC02D) else Color.White
        )
    }
}