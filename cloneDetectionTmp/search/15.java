private void manufactureWeapons(){
  Weapon weapon;
  weapon=blacksmith.manufactureWeapon(WeaponType.SPEAR);
  System.out.println(weapon);
  weapon=blacksmith.manufactureWeapon(WeaponType.AXE);
  System.out.println(weapon);
}
