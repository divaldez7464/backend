from django.contrib import admin
from django.urls import path, include
from django.contrib.auth import views as auth_views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('auth/', include('social_django.urls', namespace='social')),  # Google OAuth
    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    path('', include('data.urls')),
]