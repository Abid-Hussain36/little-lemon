package com.example.littlelemon.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.littlelemon.LLDatabase
import com.example.littlelemon.MenuItemRoom
import com.example.littlelemon.Profile
import com.example.littlelemon.R

@Composable
fun Home(navController: NavHostController, database: LLDatabase){
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    var searchText by remember {
        mutableStateOf("")
    }
    var currentCategory by remember {
        mutableStateOf("")
    }
    var currentElem by remember {
        mutableStateOf("")
    }

     var menuItems: List<MenuItemRoom> =
             if (searchText.isNotEmpty()) {
                 databaseMenuItems.filter { it.title.contains(searchText, ignoreCase = true) }
             }
             else if(currentCategory.isNotEmpty()){
                 databaseMenuItems.filter { it.category.contains(currentCategory, ignoreCase = true) }
             }
             else{
                 databaseMenuItems
             }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier.size(150.dp, 75.dp)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp, 75.dp)
                        .clickable {
                            navController.navigate(Profile.route)
                        },
                    alignment = Alignment.TopEnd,
                )
            }
        }
        HeroSection(searchText, currentCategory, currentElem, { typedText: String -> searchText = typedText }, {categoryName: String ->
                currentCategory = categoryName
        })
        MenuItemsDisplay(mItems = menuItems)
    }
}

@Composable
fun HeroSection(searchText: String, currCat: String, currElem: String, changeSearchText: (String) -> Unit, sortIntoCategory: (String) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.45f)
            .background(Color.DarkGray)
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Little Lemon",
                fontSize = 44.sp,
                color = Color.Yellow,
                modifier = Modifier.offset(y = -10.dp)
            )
            Text(
                text = "Chicago",
                fontSize = 30.sp,
                color = Color.LightGray,
                modifier = Modifier.offset(y = -10.dp)
            )
            Text(
                text = "We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist " + currCat + " " + currElem,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp, bottom = 30.dp)
            )
            OutlinedTextField(
                value = searchText,
                onValueChange = { changeSearchText(it) },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {Text(text = "Enter Search Query")}
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.3f)
            .background(Color.White)
            .padding(start = 20.dp, end = 10.dp)
    ) {
        Column(){
            Text(text = "Order for Delivery!")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                items(
                    items = listOf("Starters", "Mains", "Desserts", "Drinks"),
                    itemContent = {category ->
                        Button(
                            onClick = {
                                            sortIntoCategory(category)
                                      },
                            modifier = Modifier
                                .fillMaxWidth(.2f)
                                .padding(end = 10.dp),
                            shape = RoundedCornerShape(40.dp),
                        ) {
                            Text(text = category)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MenuItemsDisplay(mItems: List<MenuItemRoom>){
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 20.dp, end = 20.dp)
    ){
        items(
            items = mItems,
            itemContent = {menuItem ->
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = menuItem.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 5.dp,bottom = 5.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Green)
                    ) {
                        Text(
                            text = menuItem.description,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .fillMaxWidth(.6f)
                                .padding(bottom = 5.dp)
                                .background(Color.Yellow)
                        )
                    }
                    Text(
                        text = "$" + menuItem.price + ".00",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )

                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier.size(150.dp, 75.dp)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp, 75.dp),
                    alignment = Alignment.TopEnd
                )
            }
        }
    }
}
