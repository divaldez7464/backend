from django.contrib import admin
from django.urls import path, include
from django.contrib.auth import views as auth_views
from . import views

urlpatterns = [
    path('newuser', views.create_user, name='create_user'),
    path('login', views.login_user, name='login_user'),
    path('logout', views.logout_user, name='logout_user'),
    path('deleteuser', views.delete_user, name='delete_user'),
    path('items', views.list_items, name='list_items'),
    path('items/list', views.show_list, name='show_list'),
    path('items/search', views.list_item, name='list_item'),
    path('items/item', views.show_item, name='show_item'),
    path('items/add', views.add_item, name='add_item'),
    path('items/remove', views.remove_item, name='remove_item'),
    path('items/update', views.update_item, name='update_item'),
]