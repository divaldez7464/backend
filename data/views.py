from django.shortcuts import render, redirect
from django.contrib.auth.models import User
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from .models import Item
from django.conf import settings

def create_user(request):
    if request.method == 'POST':
        # Logic to create a new user (optional for your flow)
        username = request.POST.get('username')
        password = request.POST.get('password')
        email = request.POST.get('email')

        if username and password and email:
            # Create user in the Django auth system
            user = User.objects.create_user(username=username, password=password, email=email)
            user.save()
            return redirect('login_user')  # Redirect to login page after creating a user
    return render(request, 'create_user.html')

def login_user(request):
    return redirect('social:begin', backend='google-oauth2')

def logout_user(request):
    logout(request)
    return redirect(settings.LOGOUT_REDIRECT_URL)

def delete_user(request):
    if request.method == 'DELETE':
        username = request.GET.get('username')
        password = request.GET.get('password')
        user = authenticate(request, username=username, password=password)
        if user is not None:
            user.delete()
            return JsonResponse({'status': 'User deleted successfully'})
        else:
            return JsonResponse({'status': 'Invalid credentials'})

@login_required
def list_items(request):
    items = Item.objects.filter(user=request.user)
    return JsonResponse({'items': list(items.values())})

def show_item(request):
    item_id = request.GET.get('itemID')
    item = Item.objects.get(id=item_id)
    return JsonResponse({'item': item})

def add_item(request):
    if request.method == 'POST':
        item_name = request.GET.get('item_name')
        url = request.GET.get('url', '')
        price = request.GET.get('price', '')
        quantity = request.GET.get('quantity', '')
        description = request.GET.get('description', '')
        
        if not item_name:
            return JsonResponse({'status': 'Item name is required'}, status=400)
        
        item = Item.objects.create(
            name=item_name,
            url=url,
            price=price,
            quantity=quantity,
            description=description,
            user=request.user
        )
        return JsonResponse({'status': 'Item added successfully', 'item': item.id})

def delete_item(request):
    if request.method == 'DELETE':
        item_id = request.GET.get('item_id')
        confirm = request.GET.get('confirm', 'no')
        
        if confirm.lower() != 'yes':
            return JsonResponse({'status': 'Confirmation required'}, status=400)
        
        try:
            item = Item.objects.get(id=item_id)
            item.delete()
            return JsonResponse({'status': 'Item deleted successfully'})
        except Item.DoesNotExist:
            return JsonResponse({'status': 'Item not found'}, status=404)

@require_http_methods(["PATCH"])
def update_item(request):
    item_id = request.GET.get('item_id')
    item_name = request.GET.get('item_name', None)
    url = request.GET.get('url', None)
    price = request.GET.get('price', None)
    quantity = request.GET.get('quantity', None)
    description = request.GET.get('description', None)
    
    try:
        item = Item.objects.get(id=item_id)
        
        if item_name is not None:
            item.name = item_name
        if url is not None:
            item.url = url
        if price is not None:
            item.price = price
        if quantity is not None:
            item.quantity = quantity
        if description is not None:
            item.description = description
        
        item.save()
        return JsonResponse({'status': 'Item updated successfully'})
    except Item.DoesNotExist:
        return JsonResponse({'status': 'Item not found'}, status=404)