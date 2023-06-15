package com.example.pointiki.presentation.start.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pointiki.R
import com.example.pointiki.models.Achievement
import com.example.pointiki.models.CountryCode
import com.example.pointiki.models.Organization
import com.example.pointiki.presentation.home.HomeViewModel
import com.example.pointiki.presentation.home.achievements.AchievementRow
import com.example.pointiki.presentation.main.MainScreenNavigation
import com.example.pointiki.presentation.utils.components.BlackAndWhiteImage
import com.example.pointiki.presentation.utils.theme.AchievementColor
import com.example.pointiki.presentation.utils.theme.PointikiTheme
import com.example.pointiki.presentation.utils.theme.PrimaryColor
import com.example.pointiki.presentation.utils.theme.SecondaryLightColor
import com.example.pointiki.utils.countryCodes
import java.util.UUID

@Composable
fun OrganizationEnterContent(
    modifier: Modifier = Modifier,
    organizations: List<Organization>,
    shouldContinue: Boolean,
    onBackClick: () -> Unit,
    updateOrganization: (Organization) -> Unit,
    onContinue: () -> Unit,
) {

    var selectedOrganization by remember { mutableStateOf<Organization?>(null) }
    val selectOrganization: (Organization) -> Unit = {
        updateOrganization.invoke(it)
        selectedOrganization = it
    }
    Surface(color = PrimaryColor) {
        Box(
            modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp)) {
            Column {
                BackButton(onClick = onBackClick)
                Spacer(Modifier.height(45.dp))

                Title(text = stringResource(id = R.string.organization_enter_title), fontSize = 32.sp)
                Spacer(Modifier.height(20.dp))

                SmallText(text = stringResource(id = R.string.organization_enter_description))

                Spacer(Modifier.height(20.dp))

                val chunkedOrganizations = organizations.chunked(3)


                LazyColumn {
                    items(chunkedOrganizations) { organizations ->
                        OrganizationsRow(organizations, selectedOrganization, selectOrganization)
                    }
                }
            }

            MainButton(
                onClick = { onContinue() },
                isEnabled = shouldContinue,
                text = stringResource(id = R.string.btn_continue),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 28.dp)
            )
        }
    }
}

@Composable
fun OrganizationsRow(
    organizations: List<Organization>,
    selectedOrganization: Organization?,
    onSelectOrganization: (Organization) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (organization in organizations) {
            OrganizationView(organization, selectedOrganization, onSelectOrganization)
        }
    }
}

@Composable
fun OrganizationView(
    organization: Organization,
    selectedOrganization: Organization?,
    onSelectOrganization: (Organization) -> Unit) {

    val isSelected = organization == selectedOrganization
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .height(150.dp),
        colors = CardDefaults.cardColors(if (isSelected) SecondaryLightColor else Color.LightGray),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onSelectOrganization.invoke(organization)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(organization.imageUrl),
                contentDescription = "Organization",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
            )
            Text(
                text = organization.name,

                )
        }
    }
}

@Preview
@Composable
private fun PhoneEnterContent_Preview() {
    PointikiTheme {
        OrganizationEnterContent(
            organizations = listOf(Organization(UUID.randomUUID(), "Kubik", "")),
            onBackClick = {},
            onContinue = {},
            updateOrganization = {},
            shouldContinue = true
        )
    }
}
